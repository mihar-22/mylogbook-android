package com.mylb.mylogbook.presentation.presenter.log

import com.mylb.mylogbook.domain.interactor.log.SaveTrip
import com.mylb.mylogbook.domain.resource.Trip
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView.FormField.END_LOCATION
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView.FormField.START_LOCATION
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class LogDetailsPresenter<T : Trip> @Inject constructor(
        private val saveTrip: SaveTrip<T>,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: LogDetailsView? = null
        set(view) {
            field = view

            attachView()
        }

    var trip: T? = null

    private var isFormValid = false

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        Timber.d("Destroying")

        disposables.dispose()
        view = null
        trip = null
    }

    private fun attachView() {
        Timber.d("Attaching view: %s", (view != null))

        disposables.clear()

        if (view != null) {
            observeFormValidationChanges()
            observeWeatherSelections()
            observeTrafficSelections()
            observeRoadSelections()
        }
    }

    private fun observeFormValidationChanges() {
        val formValidationChanges = view!!.formValidationChanges.subscribe {
            view!!.enableSubmitButton(it)

            isFormValid = it
        }

        disposables.add(formValidationChanges)
    }

    private fun observeWeatherSelections() {
        val weatherSelections = view!!.weatherSelections.subscribe {
            val contains = trip!!.weather.contains(it)

            if (!contains) trip!!.weather.add(it) else trip!!.weather.remove(it)

            view!!.selectWeather(it, !contains)

            view!!.enableSubmitButton(trip!!.weather.isNotEmpty() && isFormValid)
        }

        disposables.add(weatherSelections)
    }

    private fun observeTrafficSelections() {
        val trafficSelections = view!!.trafficSelections.subscribe {
            val contains = trip!!.traffic.contains(it)

            if (!contains) trip!!.traffic.add(it) else trip!!.traffic.remove(it)

            view!!.selectTraffic(it, !contains)

            view!!.enableSubmitButton(trip!!.traffic.isNotEmpty() && isFormValid)
        }

        disposables.add(trafficSelections)
    }

    private fun observeRoadSelections() {
        val roadSelections = view!!.roadSelections.subscribe {
            val contains = trip!!.road.contains(it)

            if (!contains) trip!!.road.add(it) else trip!!.road.remove(it)

            view!!.selectRoad(it, !contains)

            view!!.enableSubmitButton(trip!!.road.isNotEmpty() && isFormValid)
        }

        disposables.add(roadSelections)
    }

    fun onSubmitButtonClick() {
        trip!!.startLocation = view!!.text(START_LOCATION)
        trip!!.endLocation = view!!.text(END_LOCATION)

        saveTrip.execute(SaveTripObserver(), trip!!)
    }

    private inner class SaveTripObserver : DisposableObserver<Unit>() {

        override fun onError(e: Throwable) { Timber.d("Failed to save trip with: %s", e.message) }

        override fun onNext(t: Unit) {
            Timber.d("Saved trip")

            view!!.finishLog()
        }

        override fun onComplete() = Unit

    }

}
