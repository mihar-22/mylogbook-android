package com.mylb.mylogbook.presentation.presenter.log

import android.location.Address
import android.os.Build
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.resource.Trip
import com.mylb.mylogbook.presentation.device.location.Location
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.log.RecordLogView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.zipWith
import org.joda.time.DateTimeZone
import org.joda.time.Period
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.location.Location as LocationUpdate

@PerAndroidComponent
class RecordLogPresenter<T : Trip> @Inject constructor(
        private val location: Location,
        private val postExecutionThread: PostExecutionThread,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: RecordLogView<T>? = null
        set(view) {
            field = view

            attachView()
        }

    var trip: T? = null

    private val locations = arrayListOf<LocationUpdate>()
    private var distance = 0.0
    private var timer = Observable.interval(1, TimeUnit.SECONDS)

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        Timber.d("Destroying")

        disposables.dispose()
        locations.clear()
        view = null
        trip = null
    }

    private fun attachView() {
        Timber.d("Attaching view: %s", (view != null))

        disposables.clear()
        locations.clear()

        if (view == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view!!.showRequestLocationPermissionDialog()
        } else {
            location.checkSettings.subscribe(CheckLocationSettingObserver())
        }

        observeStopButtonClicks()
    }

    fun onRequestLocationPermissionResult(isGranted: Boolean) {
        Timber.d("Location permission is granted: %s", isGranted)

        if (!isGranted) {
            view!!.cancelRecording()
            return
        }

        location.checkSettings.subscribe(CheckLocationSettingObserver())
    }

    private fun observeStopButtonClicks() {
        val stopButtonClicks = view!!.stopButtonClicks.subscribe { stop() }

        disposables.add(stopButtonClicks)
    }

    private fun updateDistance() {
        if (locations.size < 2) return

        distance += locations[locations.lastIndex - 1].distanceTo(locations.last())

        view!!.updateDistance(distance)
    }

    private fun start() {
        trip!!.startedAt = Network.now
        startTimer()
    }

    private fun startTimer() {
        val timer = this.timer
                .observeOn(postExecutionThread.scheduler)
                .subscribe { view!!.updateTime(Period(trip!!.startedAt, Network.now)) }

        disposables.add(timer)
    }

    private fun stop() {
        disposables.clear()

        trip!!.distance = (Math.round(distance * 100) / 100).toDouble()
        trip!!.endedAt = Network.now
        trip!!.timeZone = DateTimeZone.getDefault()
        trip!!.startLatitude = locations[0].latitude
        trip!!.startLongitude = locations[0].longitude
        trip!!.endLatitude = locations.last().latitude
        trip!!.endLongitude = locations.last().longitude
        trip!!.updateLightConditions()

        recordLocationNames()
    }

    private fun recordLocationNames() {
        val reverseGeocode = location.reverseGeocode(locations.first())
                .zipWith(location.reverseGeocode(locations.last())) { start, end -> Pair(start, end) }
                .doOnSubscribe { view!!.showLoading() }
                .subscribeWith(ReverseGeocodeObserver())

        disposables.add(reverseGeocode)
    }

    private inner class ReverseGeocodeObserver() : DisposableMaybeObserver<Pair<Address, Address>>() {

        override fun onSuccess(addresses: Pair<Address, Address>) {
            view!!.hideLoading()

            trip!!.startLocation = addresses.first.locality ?: ""
            trip!!.endLocation = addresses.second.locality ?: ""

            view!!.stop(trip!!, locations)
        }

        override fun onError(e: Throwable) {
            view!!.hideLoading()

            view!!.stop(trip!!, locations)
        }

        override fun onComplete() = Unit

    }

    private inner class CheckLocationSettingObserver() : DisposableSingleObserver<Boolean>() {

        override fun onSuccess(isValid: Boolean) {
            Timber.d("Location settings is valid: %s", isValid)

            if (!isValid) {
                view!!.cancelRecording()
                return
            }

            val locationUpdates = location.updates.subscribeWith(LocationUpdatesObserver())

            disposables.add(locationUpdates)

            start()
        }

        override fun onError(e: Throwable) {
            Timber.d("Failed to check location permission with: %s", e.message)
        }

    }

    private inner class LocationUpdatesObserver() : DisposableObserver<LocationUpdate>() {

        override fun onComplete() = Unit

        override fun onNext(location: LocationUpdate) {
            locations.add(location)
            updateDistance()
        }

        override fun onError(e: Throwable) { Timber.d("Failed updating location with: %s", e.message) }

    }

}
