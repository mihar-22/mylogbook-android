package com.mylb.mylogbook.presentation.device.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.patloew.rxlocation.RxLocation
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@PerAndroidComponent
class Location @Inject constructor(
        private val locator: RxLocation,
        private val threadExecutor: ThreadExecutor,
        private val postExecutionThread: PostExecutionThread
) {

    private val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)

    @SuppressLint("MissingPermission")
    val lastLocation = locator.location()
            .lastLocation()
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)

    @SuppressLint("MissingPermission")
    val updates = locator.location()
            .updates(locationRequest)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)

    val checkSettings = locator.settings().checkAndHandleResolution(locationRequest)

    fun reverseGeocode(location: Location) = locator.geocoding()
            .fromLocation(location)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)

}
