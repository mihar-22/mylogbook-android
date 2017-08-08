package com.mylb.mylogbook.presentation.ui.fragment.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.itemSelections
import com.jakewharton.rxbinding2.widget.textChanges
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.log.LogPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.SimpleFragmentCompanion
import com.mylb.mylogbook.presentation.ui.view.log.LogView
import com.mylb.mylogbook.presentation.ui.view.log.LogView.FormField.ODOMETER
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_log.*
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

class LogFragment : BaseFragment(), LogView<Car, Supervisor> {

    @Inject override lateinit var presenter: LogPresenter<Car, Supervisor>
    @Inject lateinit var userCache: UserCache

    override val odometerTextChanges: Observable<CharSequence>
        get() = odometerEditText.textChanges()

    override val carSelections: Observable<Int>
        get() = carSpinner.itemSelections()

    override val supervisorSelections: Observable<Int>
        get() = supervisorSpinner.itemSelections()

    override val nextButtonClicks: Observable<Unit>
        get() = nextButton.clicks()

    override var formattingInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.log_prepare_title)
        appCompatActivity.supportActionBar!!.show()

        return inflater.inflate(R.layout.fragment_log, container, false)!!
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavigation()

        presenter.view = this
        presenter.load()

        unpackUserCache()
    }

    private fun unpackUserCache() { setOdometerForCar(0) }

    override fun textInputLayout(field: LogView.FormField) = when(field) {
        ODOMETER -> odometerTextInputLayout
        else -> null
    }

    override fun renderCarsSpinner(cars: List<Car>) {
        val adapter = ArrayAdapter<String>(
                activity,
                R.layout.spinner_item,
                cars.map { it.name }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        carSpinner.adapter = adapter

    }

    override fun renderSupervisorsSpinner(supervisors: List<Supervisor>) {
        val adapter = ArrayAdapter<String>(
                activity,
                R.layout.spinner_item,
                supervisors.map { it.name }
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        supervisorSpinner.adapter = adapter
    }

    override fun formatOdometerText(text: CharSequence) {
        if (text.isEmpty()) return

        formattingInProgress = true

        val number = text.toString().replace(",", "").toInt()
        val formatter = DecimalFormat("#,###,###")
        val formattedNumber = formatter.format(number)

        odometerEditText.setText(formattedNumber)
        odometerEditText.setSelection(formattedNumber.length)

        formattingInProgress = false
    }

    override fun setOdometerForCar(position: Int) {
        val odometer = ((userCache.odometers[position+1] ?: 0) / 1000).toString()

        odometerEditText.setText(odometer)
    }

    override fun navigateToRecording(car: Car, supervisor: Supervisor) {
        Timber.d("Navigating to recording (car: %d | supervisor: %d)", car.id, supervisor.id)

        val odometer = odometerEditText.text.trim().toString().replace(",", "").toInt()

        val trip = Trip()
        trip.carId = car.id
        trip.supervisorId = supervisor.id
        trip.odometer = odometer

        cacheOdometer(car, odometer)

        RecordLogFragment.start(this, false) {
            it.trip = trip
            it.car = car
            it.supervisor = supervisor
        }
    }

    private fun cacheOdometer(car: Car, odometer: Int) {
        val odometers = userCache.odometers
        odometers[car.id] = (odometer * 1000)
        userCache.odometers = odometers
    }

    override fun enableNextButton(isEnabled: Boolean) { nextButton.isEnabled = isEnabled }

    companion object Builder : SimpleFragmentCompanion(LogFragment::class) {

        fun build() = LogFragment()

    }

}