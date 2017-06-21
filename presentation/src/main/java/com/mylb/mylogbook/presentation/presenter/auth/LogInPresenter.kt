package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.interactor.auth.LogUserIn
import com.mylb.mylogbook.domain.interactor.auth.LogUserIn.Params.Credentials
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword.Params.Requester
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.Field.EMAIL
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.Field.PASSWORD
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.ValidationRule
import com.mylb.mylogbook.presentation.validation.Validator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@PerAndroidComponent
class LogInPresenter @Inject constructor(
        private val logUserIn: LogUserIn,
        private val requestNewPassword: RequestNewPassword,
        private val disposables: CompositeDisposable
) : Presenter, ValidatingView.Presenter<LogInView.Field> {

    var view: LogInView? = null
        set(view) {
            Timber.d("Setting view")

            field = view

            attachView()
        }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        Timber.d("Destroying")

        disposables.dispose()
        view = null
    }

    private fun attachView() {
        if (view == null) {
            Timber.d("View is null")

            disposables.clear()
            return
        }

        observeValidationChanges()
        observeSubmitButtonClicks()
        observeForgotPasswordButtonClicks()
    }

    override fun validationRules(field: LogInView.Field): ArrayList<ValidationRule> {
        val rules = ArrayList<ValidationRule>()

        rules.add(Validator.Required())

        when (field) {
            EMAIL -> rules.add(Validator.Email())
            PASSWORD -> rules.add(Validator.MinLength(6))
        }

        return rules
    }

    fun onValidationResult(field: LogInView.Field, isValid: Boolean) {
        when (field) {
            EMAIL -> view!!.enableForgotPasswordButton(isValid)
            else -> { /* no-op */ }
        }
    }

    override fun observeValidationChanges() {
        Timber.d("Observing validation changes")

        val validationChanges = Validator.validationChanges(
                view!!,
                this::validationRules,
                this::onValidationResult
        ).subscribe { isFormValid -> view!!.enableSubmitButton(isFormValid) }

        disposables.add(validationChanges)
    }

    fun observeSubmitButtonClicks() {
        Timber.d("Observing submit button clicks")

        val submitButtonClicks = view!!.submitButtonClicks.subscribe {
            view!!.showLoading()

            logUserIn.execute(
                    LogUserInObserver(),
                    Credentials(
                            view!!.text(EMAIL).toString(),
                            view!!.text(PASSWORD).toString()
                    )
            )
        }

        disposables.add(submitButtonClicks)
    }

    fun observeForgotPasswordButtonClicks() {
        Timber.d("Observing forgot password button clicks")

        val forgotPasswordButtonClicks = view!!.forgotPasswordButtonClicks.subscribe {
            view!!.showLoading()

            requestNewPassword.execute(
                    RequestNewPasswordObserver(),
                    Requester(view!!.text(EMAIL).toString())
            )
        }

        disposables.add(forgotPasswordButtonClicks)
    }

    private inner class LogUserInObserver : DisposableObserver<Response<Map<String, String>>>() {

        override fun onNext(t: Response<Map<String, String>>) {
            Timber.d("User logged in")

            view!!.hideLoading()
            view!!.navigateToDashboard()
        }

        override fun onComplete() = Unit

        override fun onError(e: Throwable) {
            Timber.d("Logging in failed with: %s", e.message)

            view!!.hideLoading()

            if (e is HttpException && e.code() == 400) view!!.showInvalidCredentialsToast()
            if (e is IOException) view!!.showConnectionTimeoutToast()
        }

    }

    private inner class RequestNewPasswordObserver : DisposableObserver<Response<Unit>>() {

        override fun onNext(t: Response<Unit>) {
            Timber.d("Forgot password mail sent")

            view!!.hideLoading()
            view!!.showForgotPasswordSentDialog()
        }

        override fun onComplete() = Unit

        override fun onError(e: Throwable) {
            Timber.d("Password reset failed with: %s", e.message)

            view!!.hideLoading()

            if (e is HttpException && e.code() == 422) view!!.showNoAccountToast()
            if (e is IOException) view!!.showConnectionTimeoutToast()
        }

    }

}
