package com.mylb.mylogbook.presentation.ui.view.log

import com.mylb.mylogbook.domain.resource.trip.encounter.Road
import com.mylb.mylogbook.domain.resource.trip.encounter.Traffic
import com.mylb.mylogbook.domain.resource.trip.encounter.Weather
import com.mylb.mylogbook.presentation.validation.Rule
import com.mylb.mylogbook.presentation.validation.ValidatableForm
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.onFormValidationChanges
import io.reactivex.Observable

interface LogDetailsView : ValidatingView<LogDetailsView.FormField> {

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    val weatherSelections: Observable<Weather.Condition>
    val trafficSelections: Observable<Traffic.Condition>
    val roadSelections: Observable<Road.Condition>

    fun selectWeather(weather: Weather.Condition, isSelected: Boolean)
    fun selectTraffic(traffic: Traffic.Condition, isSelected: Boolean)
    fun selectRoad(road: Road.Condition, isSelected: Boolean)
    fun text(field: FormField): String
    fun enableSubmitButton(isEnabled: Boolean)
    fun finishLog()

    enum class FormField : ValidatableForm {

        START_LOCATION, END_LOCATION;

        override fun rules() = when(this) {
            START_LOCATION -> listOf(Rule.Required())
            END_LOCATION -> listOf(Rule.Required())
        }

    }

}
