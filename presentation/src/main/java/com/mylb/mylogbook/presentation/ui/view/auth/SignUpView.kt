package com.mylb.mylogbook.presentation.ui.view.auth

import com.mylb.mylogbook.presentation.ui.view.LoadingView
import com.mylb.mylogbook.presentation.validation.*
import io.reactivex.Observable

interface SignUpView : LoadingView, ValidatingView<SignUpView.FormField> {

    val submitButtonClicks: Observable<Unit>

    val formValidationChanges: Observable<Boolean>
        get() = onFormValidationChanges(this)

    fun text(field: FormField): String
    fun enableSubmitButton(isEnabled: Boolean)
    fun showEmailTakenToast()
    fun showConnectionTimeoutToast()
    fun navigateToLogIn()

    enum class FormField : ValidatableForm {

        NAME, EMAIL, PASSWORD, BIRTHDATE;

        override fun rules() = when(this) {
            NAME -> listOf(Rule.Required(), Rule.MaxLength(100))
            EMAIL -> listOf(Rule.Required(), Rule.Email())
            PASSWORD -> listOf(Rule.Required(), Rule.MinLength(6))
            BIRTHDATE -> listOf(Rule.Date())
        }

    }

}
