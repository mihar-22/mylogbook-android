package com.mylb.mylogbook.presentation.ui.fragment.log

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.log.RecordLogPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.FragmentCompanion
import com.mylb.mylogbook.presentation.ui.view.log.RecordLogView
import com.mylb.mylogbook.presentation.util.logRecordingDrawable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_record_log.*
import kotlinx.android.synthetic.main.view_progress_bar.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Parcelable
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import timber.log.Timber
import javax.inject.Inject

class RecordLogFragment : BaseFragment(), RecordLogView<Trip> {

    @Inject lateinit override var presenter: RecordLogPresenter<Trip>

    override val stopButtonClicks: Observable<Unit>
        get() = stopButton.clicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)

        appCompatActivity.supportActionBar!!.hide()
        hideBottomNavigation()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.log_recording)

        return inflater.inflate(R.layout.fragment_record_log, container, false)!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unpackIntent()

        if (isGooglePlayServicesAvailable()) presenter.view = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val googlePlayUpdate = ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
        val cancelledGooglePlayUpdate = ConnectionResult.CANCELED

        if (requestCode == googlePlayUpdate && resultCode == cancelledGooglePlayUpdate) {
            cancelRecording()

            return
        }

        if (requestCode == googlePlayUpdate && isGooglePlayServicesAvailable()) presenter.view = this
    }

    private fun unpackIntent() {
        intent.options {
            presenter.trip = it.trip
            recordingImageView.setImageDrawable(activity.logRecordingDrawable(it.car!!.body))
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun showRequestLocationPermissionDialog() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        presenter.onRequestLocationPermissionResult(
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        )
    }

    override fun showLoading() {
        stopButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() { progressBar.visibility = View.GONE }

    override fun stop(trip: Trip, locations: ArrayList<Location>) {
        Timber.d("Navigating to log details")

        var car: Car? = null
        var supervisor: Supervisor? = null

        intent.options {
            car = it.car
            supervisor = it.supervisor
        }

        LogDetailsFragment.start(this, false) {
            it.trip = trip
            it.car = car
            it.supervisor = supervisor
            it.locations = java.util.ArrayList(locations)
        }
    }

    override fun cancelRecording() { LogFragment.start(this, false) }

    override fun showCancelRecordingDialog() {
        AlertDialog.Builder(activity)
                .setTitle(getString(R.string.log_cancel_recording_title))
                .setMessage(getString(R.string.log_cancel_recording_message))
                .setPositiveButton(
                        getString(R.string.action_yes),
                        { _, _ -> cancelRecording() }
                )
                .setNegativeButton(getString(R.string.action_no), { dialog, _ -> dialog.dismiss() })
                .show()
    }

    override fun updateDistance(distance: Double) {
        distanceTextView.text = "${"%.2f".format(distance / 1000)} km"
    }

    override fun onBackButtonPressed(): Boolean {
        showCancelRecordingDialog()

        return false
    }

    override fun updateTime(period: Period) {
        val formatter = PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" d")
                .appendSeparator(" ")
                .appendMinutes()
                .appendSuffix(" m")
                .appendSeparator(" ")
                .appendSeconds()
                .appendSuffix(" s")
                .toFormatter()

        timeTextView.text = period.toString(formatter)
    }

    companion object Builder : FragmentCompanion<IntentOptions>(IntentOptions, RecordLogFragment::class) {

        fun build() = RecordLogFragment()

    }

    object IntentOptions {
        var Intent.trip by IntentExtra.Parcelable<Trip>()
        var Intent.car by IntentExtra.Parcelable<Car>()
        var Intent.supervisor by IntentExtra.Parcelable<Supervisor>()
    }

}
