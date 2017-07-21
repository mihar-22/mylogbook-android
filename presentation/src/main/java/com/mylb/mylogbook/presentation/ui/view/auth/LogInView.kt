package com.mylb.mylogbook.presentation.ui.view.auth

import com.mylb.mylogbook.presentation.ui.view.LoadingView
import com.mylb.mylogbook.presentation.validation.*
import io.reactivex.Observable

interface LogInView : LoadingView, ValidatingView<LogInView.FormField> {

    val submitButtonClicks: Observable<Unit>
    val forgotPasswordButtonClicks: Observable<Unit>
    val emailValidationChanges: Observable<Boolean>

    val formValidationChanges: Observable<Boolean>
            get() = onFormValidationChanges(this)

    fun text(field: FormField): String
    fun enableSubmitButton(isEnabled: Boolean)
    fun enableForgotPasswordButton(isEnabled: Boolean)
    fun showInvalidCredentialsToast()
    fun showConnectionTimeoutToast()
    fun showNoAccountToast()
    fun showEmailConfirmationDialog()
    fun showForgotPasswordSentDialog()
    fun navigateToDashboard()

    enum class FormField : ValidatableForm {

        EMAIL, PASSWORD;

        override fun rules() = when(this) {
            EMAIL -> listOf(Rule.Required(), Rule.Email())
            PASSWORD -> listOf(Rule.Required(), Rule.MinLength(6))
        }

    }

}