package com.mylb.mylogbook.presentation.ui.view.log

import android.location.Location
import com.mylb.mylogbook.domain.resource.Trip
import com.mylb.mylogbook.presentation.ui.view.LoadingView
import io.reactivex.Observable
import org.joda.time.Period

interface RecordLogView<in T : Trip> : LoadingView {

    val stopButtonClicks: Observable<Unit>

    fun showRequestLocationPermissionDialog()
    fun cancelRecording()
    fun updateDistance(distance: Double)
    fun updateTime(period: Period)
    fun showCancelRecordingDialog()
    fun stop(trip: T, locations: ArrayList<Location>)

}
