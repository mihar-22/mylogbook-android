package com.mylb.mylogbook.presentation.ui.activity.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.presenter.auth.SignUpPresenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.view_progress_bar.*
import javax.inject.Inject

@PerActivity
class SignUpActivity : BaseActivity(), SignUpView {

    @Inject lateinit var presenter: SignUpPresenter

    val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(activityModule)
                .build()

    override val submitButtonClicks: Observable<Unit>
            get() = submitButton.clicks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        component.inject(this)

        presenter.view = this
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE

        enableSubmitButton(false)
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE

        enableSubmitButton(true)
    }

    override fun showEmailTakenToast() = showToast(getString(R.string.error_email_taken))

    override fun showConnectionTimeoutToast() = showToast(getString(R.string.error_connection_timeout))

    fun showToast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun showSignUpSuccessAlert() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.sign_up_success_title))
                .setMessage(getString(R.string.sign_up_success_message))
                .setPositiveButton(
                        getString(R.string.open_mail),
                        { _, _ -> navigator.navigateToDefaultMail(this) }
                )
                .setNegativeButton(
                        getString(R.string.log_in),
                        { _, _ -> navigator.navigateToLogIn(this) }
                )
                .show()
    }

    override fun text(field: SignUpView.Field): CharSequence {
        when (field) {
            SignUpView.Field.NAME -> { return nameEditText.text.trim() }
            SignUpView.Field.EMAIL -> { return emailEditText.text.trim() }
            SignUpView.Field.PASSWORD -> { return passwordEditText.text.trim() }
            SignUpView.Field.BIRTHDATE -> { return birthdateEditText.text.trim() }
        }
    }

    override fun textChanges(field: SignUpView.Field): Observable<CharSequence> {
        when (field) {
            SignUpView.Field.NAME -> { return nameEditText.textChanges() }
            SignUpView.Field.EMAIL -> { return emailEditText.textChanges() }
            SignUpView.Field.PASSWORD -> { return passwordEditText.textChanges() }
            SignUpView.Field.BIRTHDATE -> { return birthdateEditText.textChanges() }
        }
    }

    override fun showError(field: SignUpView.Field, error: CharSequence?) {
        val isErrorEnabled = (error != null)

        when (field) {
            SignUpView.Field.NAME -> {
                nameTextInputLayout.error = error
                nameTextInputLayout.isErrorEnabled = isErrorEnabled
            }

            SignUpView.Field.EMAIL -> {
                emailTextInputLayout.error = error
                emailTextInputLayout.isErrorEnabled = isErrorEnabled
            }

            SignUpView.Field.PASSWORD -> {
                passwordTextInputLayout.error = error
                passwordTextInputLayout.isErrorEnabled = isErrorEnabled
            }

            SignUpView.Field.BIRTHDATE -> {
                birthdateTextInputLayout.error = error
                birthdateTextInputLayout.isErrorEnabled = isErrorEnabled
            }
        }
    }

    override fun enableSubmitButton(isEnabled: Boolean) {
        submitButton.isEnabled = isEnabled
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }
}
