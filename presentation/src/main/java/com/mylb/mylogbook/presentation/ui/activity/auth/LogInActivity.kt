package com.mylb.mylogbook.presentation.ui.activity.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.cache.SettingsCache
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.SystemIntent
import com.mylb.mylogbook.presentation.device.authenticator.Authenticator
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.auth.LogInPresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import com.mylb.mylogbook.presentation.ui.activity.setup.SetupLicenseActivity
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.FormField.EMAIL
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.FormField.PASSWORD
import com.mylb.mylogbook.presentation.validation.validationChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.view_progress_bar.*
import me.eugeniomarletti.extras.ActivityCompanion
import me.eugeniomarletti.extras.intent.IntentExtra
import me.eugeniomarletti.extras.intent.base.Boolean
import me.eugeniomarletti.extras.intent.base.String
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class LogInActivity : BaseActivity(), LogInView {

    @Inject lateinit var auth: Auth
    @Inject lateinit var userCache: UserCache
    @Inject lateinit var settings: SettingsCache
    @Inject override lateinit var presenter: LogInPresenter

    private val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(activityModule)
                .build()

    override val submitButtonClicks: Observable<Unit>
        get() = submitButton.clicks()

    override val forgotPasswordButtonClicks: Observable<Unit>
        get() = forgotPasswordButton.clicks()

    override val emailValidationChanges: Observable<Boolean>
        get() = emailTextInputLayout.validationChanges(EMAIL.rules())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        component.inject(this)
        presenter.view = this

        unpackCache()
        unpackIntent()
        unpackAuthenticatorIntent()
    }

    private fun unpackCache() {
        if (!userCache.email.isNullOrEmpty()) { emailEditText.setText(userCache.email) }
    }

    private fun unpackIntent() {
        intent.options {
            if (intent.email != null) emailEditText.setText(intent.email)
            if (intent.password != null) passwordEditText.setText(intent.password)
            if (intent.justSignedUp == true) showEmailConfirmationDialog()
        }
    }

    private fun unpackAuthenticatorIntent() {
        with (Authenticator.IntentOptions) {
            if (intent.authenticatorResponse != null) {
                if (intent.accountName != null) emailEditText.setText(intent.accountName)

                title = getString(R.string.app_name)
                submitButton.text = getString(R.string.auth_add_account)

                auth.processRequest(intent)
            }
        }
    }

    override fun navigateToDashboard() {
        with (Authenticator.IntentOptions) {
            if (intent.authenticatorResponse != null) {
                finish()

                return
            }
        }

        if (settings.isSetup) {
            Timber.d("Navigating to dashboard")

            MainActivity.start(this)
        } else {
            Timber.d("Navigating to setup")

            SetupLicenseActivity.start(this)
        }

        finish()
    }

    override fun showLoading() {
        hideSoftKeyboard()
        progressBar.visibility = View.VISIBLE
        enableSubmitButton(false)
        enableForgotPasswordButton(false)
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        enableSubmitButton(true)
        enableForgotPasswordButton(true)
    }

    override fun text(field: LogInView.FormField) = when (field) {
        EMAIL -> emailEditText.text.trim().toString()
        PASSWORD -> passwordEditText.text.trim().toString()
    }

    override fun textInputLayout(field: LogInView.FormField) = when(field) {
        EMAIL -> emailTextInputLayout
        PASSWORD -> passwordTextInputLayout
    }

    override fun showEmailConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.sign_up_success_title))
            .setMessage(getString(R.string.sign_up_success_message))
            .setPositiveButton(
                    getString(R.string.system_open_mail),
                    { _, _ -> SystemIntent.openMail(this) }
            )
            .show()
    }

    override fun showForgotPasswordSentDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.password_reset_sent_title))
                .setMessage(getString(R.string.password_reset_sent_message))
                .setPositiveButton(
                        getString(R.string.system_open_mail),
                        { _, _ -> SystemIntent.openMail(this) }
                )
                .show()
    }

    override fun showInvalidCredentialsToast() = showToast(getString(R.string.error_invalid_credentials))

    override fun showConnectionTimeoutToast() = showToast(getString(R.string.error_connection_timeout))

    override fun showNoAccountToast() = showToast(getString(R.string.error_no_account))

    override fun enableSubmitButton(isEnabled: Boolean) { submitButton.isEnabled = isEnabled }

    override fun enableForgotPasswordButton(isEnabled: Boolean) {
        forgotPasswordButton.isEnabled = isEnabled
    }

    companion object : ActivityCompanion<IntentOptions>(IntentOptions, LogInActivity::class)

    object IntentOptions {
        var Intent.email by IntentExtra.String()
        var Intent.password by IntentExtra.String()
        var Intent.justSignedUp by IntentExtra.Boolean()
    }

}
