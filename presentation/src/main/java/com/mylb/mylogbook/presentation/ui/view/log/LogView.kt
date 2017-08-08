package com.mylb.mylogbook.presentation.ui.view.log

import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.validation.Rule
import com.mylb.mylogbook.presentation.validation.ValidatableForm
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.onFormValidationChanges
import io.reactivex.Observable

interface LogView<in C : Car, in S : Supervisor> : ValidatingView<LogView.FormField> {

    val odometerTextChanges: Observable<CharSequence>
    val carSelections: Observable<Int>
    val supervisorSelections: Observable<Int>
    val nextButtonClicks: Observable<Unit>
    var formattingInProgress: Boolean

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    fun setOdometerForCar(position: Int)
    fun formatOdometerText(text: CharSequence)
    fun renderCarsSpinner(cars: List<C>)
    fun renderSupervisorsSpinner(supervisors: List<S>)
    fun enableNextButton(isEnabled: Boolean)
    fun navigateToRecording(car: C, supervisor: S)

    enum class FormField : ValidatableForm {

        CAR, SUPERVISOR, ODOMETER;

        override fun rules() = when(this) {
            CAR -> null
            SUPERVISOR -> null
            ODOMETER -> listOf(Rule.Required(), Rule.Min(0), Rule.Max(999999))
        }

    }

}
