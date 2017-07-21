package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.interactor.auth.LogUserIn
import com.mylb.mylogbook.domain.interactor.auth.LogUserIn.Params.Credentials
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword
import com.mylb.mylogbook.domain.interactor.auth.RequestNewPassword.Params.Requester
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView
import com.mylb.mylogbook.presentation.ui.view.auth.LogInView.FormField.*
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
) : Presenter {

    var view: LogInView? = null
        set(view) {
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
        Timber.d("Attaching view: %s", (view != null))

        disposables.clear()

        if (view != null) {
            observeEmailValidationChanges()
            observeFormValidationChanges()
            observeSubmitButtonClicks()
            observeForgotPasswordButtonClicks()
        }
    }

    private fun observeEmailValidationChanges() {
        val emailValidationChanges = view!!.emailValidationChanges.subscribe {
            view!!.enableForgotPasswordButton(it)
        }

        disposables.add(emailValidationChanges)
    }

    private fun observeFormValidationChanges() {
        val formValidationChanges = view!!.formValidationChanges.subscribe {
            view!!.enableSubmitButton(it)
        }

        disposables.add(formValidationChanges)
    }

    private fun observeSubmitButtonClicks() {
        val submitButtonClicks = view!!.submitButtonClicks.subscribe {
            view!!.showLoading()

            logUserIn.execute(
                    LogUserInObserver(),
                    Credentials(view!!.text(EMAIL), view!!.text(PASSWORD))
            )
        }

        disposables.add(submitButtonClicks)
    }

    private fun observeForgotPasswordButtonClicks() {
        val forgotPasswordButtonClicks = view!!.forgotPasswordButtonClicks.subscribe {
            view!!.showLoading()

            requestNewPassword.execute(
                    RequestNewPasswordObserver(),
                    Requester(view!!.text(EMAIL))
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
