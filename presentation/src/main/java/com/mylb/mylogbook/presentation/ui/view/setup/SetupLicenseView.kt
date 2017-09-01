package com.mylb.mylogbook.presentation.ui.view.setup

import com.mylb.mylogbook.presentation.validation.Rule
import com.mylb.mylogbook.presentation.validation.ValidatableForm
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.onFormValidationChanges
import io.reactivex.Observable

interface SetupLicenseView : ValidatingView<SetupLicenseView.FormField> {

    val submitButtonClicks: Observable<Unit>

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    fun enableSubmitButton(isEnabled: Boolean)
    fun navigateToStateSetup()

    enum class FormField : ValidatableForm {

        RECEIVED_ON;

        override fun rules() = when(this) {
            RECEIVED_ON -> listOf(Rule.Date())
        }

    }

}
