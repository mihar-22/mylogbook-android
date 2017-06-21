package com.mylb.mylogbook.presentation.ui.view.auth

import com.mylb.mylogbook.presentation.ui.view.LoadingView
import com.mylb.mylogbook.presentation.validation.ValidatingView
import io.reactivex.Observable

interface LogInView : LoadingView, ValidatingView<LogInView.Field> {

    val submitButtonClicks: Observable<Unit>
    val forgotPasswordButtonClicks: Observable<Unit>

    fun text(field: LogInView.Field): CharSequence
    fun enableSubmitButton(isEnabled: Boolean)
    fun enableForgotPasswordButton(isEnabled: Boolean)
    fun showInvalidCredentialsToast()
    fun showConnectionTimeoutToast()
    fun showNoAccountToast()
    fun showEmailConfirmationDialog()
    fun showForgotPasswordSentDialog()
    fun navigateToDashboard()

    enum class Field { EMAIL, PASSWORD }

}