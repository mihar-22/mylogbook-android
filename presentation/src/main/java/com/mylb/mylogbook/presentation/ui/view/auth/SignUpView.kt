package com.mylb.mylogbook.presentation.ui.view.auth

import com.mylb.mylogbook.presentation.ui.view.LoadingView
import com.mylb.mylogbook.presentation.validation.ValidatingView
import io.reactivex.Observable

interface SignUpView : LoadingView, ValidatingView<SignUpView.Field> {

    val submitButtonClicks: Observable<Unit>

    fun text(field: SignUpView.Field): CharSequence
    fun enableSubmitButton(isEnabled: Boolean)
    fun showEmailTakenToast()
    fun showConnectionTimeoutToast()
    fun navigateToLogIn()

    enum class Field { NAME, EMAIL, PASSWORD, BIRTHDATE }

}
