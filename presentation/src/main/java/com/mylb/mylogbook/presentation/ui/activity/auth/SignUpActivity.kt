package com.mylb.mylogbook.presentation.ui.activity.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.auth.SignUpPresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.FormField.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.view_progress_bar.*
import me.eugeniomarletti.extras.SimpleActivityCompanion
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class SignUpActivity : BaseActivity(), SignUpView {

    @Inject override lateinit var presenter: SignUpPresenter

    private val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(activityModule)
                .build()

    override val submitButtonClicks: Observable<Unit>
            get() = submitButton.clicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        component.inject(this)
        presenter.view = this
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        enableSubmitButton(false)
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        enableSubmitButton(true)
    }

    override fun navigateToLogIn() {
        Timber.d("Navigating to log in")

        LogInActivity.start(this) { intent ->
            intent.email = text(EMAIL)
            intent.password = text(PASSWORD)
            intent.justSignedUp = true
        }

        finish()
    }

    override fun showEmailTakenToast() = showToast(getString(R.string.error_email_taken))

    override fun showConnectionTimeoutToast() = showToast(getString(R.string.error_connection_timeout))

    override fun text(field: SignUpView.FormField) = when (field) {
        NAME -> nameEditText.text.trim().toString()
        EMAIL -> emailEditText.text.trim().toString()
        PASSWORD -> passwordEditText.text.trim().toString()
        BIRTHDATE -> birthdateEditText.text.trim().toString()
    }

    override fun textInputLayout(field: SignUpView.FormField) = when(field) {
        NAME -> nameTextInputLayout
        EMAIL -> emailTextInputLayout
        PASSWORD -> passwordTextInputLayout
        BIRTHDATE -> birthdateTextInputLayout
    }

    override fun enableSubmitButton(isEnabled: Boolean) { submitButton.isEnabled = isEnabled }

    companion object : SimpleActivityCompanion(SignUpActivity::class)

}
