package com.mylb.mylogbook.presentation.ui.activity.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.auth.SignUpPresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.Field.*
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
            intent.email = text(EMAIL).toString()
            intent.password = text(PASSWORD).toString()
            intent.justSignedUp = true
        }

        finish()
    }

    override fun showEmailTakenToast() = showToast(getString(R.string.error_email_taken))

    override fun showConnectionTimeoutToast() = showToast(getString(R.string.error_connection_timeout))

    private fun showToast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun text(field: SignUpView.Field) = when (field) {
        NAME -> nameEditText.text.trim()
        EMAIL -> emailEditText.text.trim()
        PASSWORD -> passwordEditText.text.trim()
        BIRTHDATE -> birthdateEditText.text.trim()
    }

    override fun textChanges(field: SignUpView.Field) = when (field) {
        NAME -> nameEditText.textChanges()
        EMAIL -> emailEditText.textChanges()
        PASSWORD -> passwordEditText.textChanges()
        BIRTHDATE -> birthdateEditText.textChanges()
    }

    override fun showError(field: SignUpView.Field, error: CharSequence?) = when (field) {
        NAME -> {
            nameTextInputLayout.error = error
            nameTextInputLayout.isErrorEnabled = (!error.isNullOrEmpty())
        }

        EMAIL -> {
            emailTextInputLayout.error = error
            emailTextInputLayout.isErrorEnabled = (!error.isNullOrEmpty())
        }

        PASSWORD -> {
            passwordTextInputLayout.error = error
            passwordTextInputLayout.isErrorEnabled = (!error.isNullOrEmpty())
        }

        BIRTHDATE -> {
            birthdateTextInputLayout.error = error
            birthdateTextInputLayout.isErrorEnabled = (!error.isNullOrEmpty())
        }
    }

    override fun enableSubmitButton(isEnabled: Boolean) { submitButton.isEnabled = isEnabled }

    companion object : SimpleActivityCompanion(SignUpActivity::class)

}
