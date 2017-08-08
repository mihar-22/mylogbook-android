package com.mylb.mylogbook.presentation.ui.fragment.log

import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.log.LogDetailsPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.FragmentCompanion
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView.FormField.END_LOCATION
import com.mylb.mylogbook.presentation.ui.view.log.LogDetailsView.FormField.START_LOCATION
import com.mylb.mylogbook.presentation.util.carDrawable
import com.mylb.mylogbook.presentation.util.enable
import com.mylb.mylogbook.presentation.util.supervisorDrawable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_log_details.*
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Parcelable
import me.eugeniomarletti.extras.intent.base.ParcelableArrayList
import timber.log.Timber
import javax.inject.Inject
import com.mylb.mylogbook.domain.location.Location as CacheLocation

class LogDetailsFragment : BaseFragment(), LogDetailsView, OnMapReadyCallback {

    @Inject override lateinit var presenter: LogDetailsPresenter<Trip>
    @Inject lateinit var userCache: UserCache

    lateinit private var trip: Trip
    lateinit private var locations: ArrayList<Location?>

    private var saveMenuItem: MenuItem? = null

    override val weatherSelections: Observable<Weather.Condition>
        get() = Observable.merge(listOf(
                weatherClearView.clicks().map { Weather.Condition.CLEAR },
                weatherRainView.clicks().map { Weather.Condition.RAIN },
                weatherThunderView.clicks().map { Weather.Condition.THUNDER },
                weatherSnowView.clicks().map { Weather.Condition.SNOW },
                weatherHailView.clicks().map { Weather.Condition.HAIL },
                weatherFogView.clicks().map { Weather.Condition.FOG }
        ))

    override val trafficSelections: Observable<Traffic.Condition>
        get() = Observable.merge(listOf(
                trafficLightView.clicks().map { Traffic.Condition.LIGHT },
                trafficModerateView.clicks().map { Traffic.Condition.MODERATE },
                trafficHeavyView.clicks().map { Traffic.Condition.HEAVY }
        ))

    override val roadSelections: Observable<Road.Condition>
        get() = Observable.merge(listOf(
                roadLocalStreetView.clicks().map { Road.Condition.LOCAL_STREET },
                roadMainRoadView.clicks().map { Road.Condition.MAIN_ROAD },
                roadFreewayView.clicks().map { Road.Condition.FREEWAY },
                roadGravelView.clicks().map { Road.Condition.GRAVEL },
                roadInnerCityView.clicks().map { Road.Condition.INNER_CITY },
                roadRuralRoadView.clicks().map { Road.Condition.RURAL_ROAD }
        ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        component.inject(this)

        appCompatActivity.supportActionBar!!.show()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.log_details)

        return inflater.inflate(R.layout.fragment_log_details, container, false)!!
    }

    private fun unpackIntent() {
        intent.options { it ->
            presenter.trip = it.trip
            trip = it.trip!!
            locations = ArrayList(it.locations!!)

            carNameTextView.text = it.car!!.name
            carTypeImageView.setImageDrawable(activity.carDrawable(it.car!!.body))

            supervisorNameTextView.text = it.supervisor!!.name
            supervisorAvatarImageView.setImageDrawable(activity.supervisorDrawable(
                    it.supervisor!!.fullGender,
                    it.supervisor!!.isAccredited
            ))

            if (trip.startLocation.isNotEmpty()) startLocationEditText.setText(trip.startLocation)
            if (trip.endLocation.isNotEmpty()) endLocationEditText.setText(trip.endLocation)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val startLocation = locations.first()!!
        val endLocation = locations.last()!!

        val startLatLng = LatLng(startLocation.latitude, startLocation.longitude)
        val endLatLng = LatLng(endLocation.latitude, endLocation.longitude)

        map.addMarker(MarkerOptions()
                .position(startLatLng)
                .title("Started here at ${trip.startedAt}")
        )

        map.addMarker(MarkerOptions()
                .position(endLatLng)
                .title("Ended here at ${trip.endedAt}")
        )

        map.addPolyline(PolylineOptions()
                .width(8F)
                .color(activity.resources.getColor(R.color.primary))
                .addAll(locations.map { LatLng(it!!.latitude, it.longitude) })
        )

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.Builder()
                .include(startLatLng)
                .include(endLatLng)
                .build(),
                50
        ))
    }

    private fun setupConditions() {
        Weather.Condition.values().forEach { selectWeather(it, false) }
        Traffic.Condition.values().forEach { selectTraffic(it, false) }
        Road.Condition.values().forEach { selectRoad(it, false) }
    }

    override fun selectWeather(weather: Weather.Condition, isSelected: Boolean) = when(weather) {
        Weather.Condition.CLEAR -> selectCondition(weatherClearView, isSelected)
        Weather.Condition.RAIN -> selectCondition(weatherRainView, isSelected)
        Weather.Condition.SNOW -> selectCondition(weatherSnowView, isSelected)
        Weather.Condition.FOG -> selectCondition(weatherFogView, isSelected)
        Weather.Condition.THUNDER -> selectCondition(weatherThunderView, isSelected)
        Weather.Condition.HAIL -> selectCondition(weatherHailView, isSelected)
    }

    override fun selectTraffic(traffic: Traffic.Condition, isSelected: Boolean) = when(traffic) {
        Traffic.Condition.LIGHT -> selectCondition(trafficLightView, isSelected)
        Traffic.Condition.MODERATE -> selectCondition(trafficModerateView, isSelected)
        Traffic.Condition.HEAVY -> selectCondition(trafficHeavyView, isSelected)
    }

    override fun selectRoad(road: Road.Condition, isSelected: Boolean) = when(road) {
        Road.Condition.LOCAL_STREET -> selectCondition(roadLocalStreetView, isSelected)
        Road.Condition.MAIN_ROAD -> selectCondition(roadMainRoadView, isSelected)
        Road.Condition.INNER_CITY -> selectCondition(roadInnerCityView, isSelected)
        Road.Condition.FREEWAY -> selectCondition(roadFreewayView, isSelected)
        Road.Condition.GRAVEL -> selectCondition(roadGravelView, isSelected)
        Road.Condition.RURAL_ROAD -> selectCondition(roadRuralRoadView, isSelected)
    }

    private fun selectCondition(layout: LinearLayout, isSelected: Boolean) {
        val child = (layout.getChildAt(1) as LinearLayout)

        (layout.getChildAt(0) as ImageView).alpha = if (isSelected) 1.0F else 0.3F
        (child.getChildAt(0) as TextView).alpha = if (isSelected) 1.0F else 0.3F
        (child.getChildAt(1) as ImageView).visibility = if (isSelected) View.VISIBLE else View.GONE
    }

    override fun textInputLayout(field: LogDetailsView.FormField) = when(field) {
        START_LOCATION -> startLocationTextInputLayout
        END_LOCATION -> endLocationTextInputLayout
    }

    override fun text(field: LogDetailsView.FormField) = when(field) {
        START_LOCATION -> startLocationEditText.text.trim().toString()
        END_LOCATION -> endLocationEditText.text.trim().toString()
    }

    override fun enableSubmitButton(isEnabled: Boolean) { saveMenuItem?.enable(isEnabled) }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        saveMenuItem = menu.findItem(R.id.saveMenuItem)
        saveMenuItem!!.enable(false)

        presenter.view = this

        unpackIntent()
        setupConditions()
        (childFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment).getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        hideSoftKeyboard()
        presenter.onSubmitButtonClick()

        return true
    }

    override fun finishLog() {
        Timber.d("Navigating to dashboard")

        cache(presenter.trip!!)

        selectBottomNavigationItem(R.id.dashboardMenuItem)
    }

    private fun cache(trip: Trip) {
        val odometers = userCache.odometers
        odometers[trip.carId] = (odometers[trip.carId]!! + trip.distance.toInt())
        userCache.odometers = odometers

        userCache.lastRoute = locations.map { CacheLocation(it!!.latitude, it.longitude) }
    }

    override fun onBackButtonPressed(): Boolean {
        showCancelRecordingDialog()

        return false
    }

    private fun showCancelRecordingDialog() {
        AlertDialog.Builder(activity)
                .setTitle(getString(R.string.log_cancel_recording_title))
                .setMessage(getString(R.string.log_cancel_recording_message))
                .setPositiveButton(
                        getString(R.string.action_yes),
                        { _, _ -> LogFragment.start(this, false) }
                )
                .setNegativeButton(getString(R.string.action_no), { dialog, _ -> dialog.dismiss() })
                .show()
    }

    companion object Builder : FragmentCompanion<IntentOptions>(IntentOptions, LogDetailsFragment::class) {

        fun build() = LogDetailsFragment()

    }

    object IntentOptions {
        var Intent.trip by IntentExtra.Parcelable<Trip>()
        var Intent.car by IntentExtra.Parcelable<Car>()
        var Intent.supervisor by IntentExtra.Parcelable<Supervisor>()
        var Intent.locations by IntentExtra.ParcelableArrayList<Location>()
    }

}
