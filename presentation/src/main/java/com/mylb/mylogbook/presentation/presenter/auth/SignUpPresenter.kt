package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.interactor.auth.SignUpUser
import com.mylb.mylogbook.domain.interactor.auth.SignUpUser.Params.NewUser
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.FormField.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@PerAndroidComponent
class SignUpPresenter @Inject constructor(
        private val signUpUser: SignUpUser,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: SignUpView? = null
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
            observeFormValidationChanges()
            observeSubmitButtonClicks()
        }
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

            signUpUser.execute(
                    SignUpUserObserver(),
                    NewUser(view!!.text(NAME), view!!.text(EMAIL), view!!.text(PASSWORD), view!!.text(BIRTHDATE))
            )
        }

        disposables.add(submitButtonClicks)
    }

    private inner class SignUpUserObserver : DisposableObserver<Response<Unit>>() {

        override fun onNext(t: Response<Unit>) {
            Timber.d("User signed up")

            view!!.hideLoading()
            view!!.navigateToLogIn()
        }

        override fun onComplete() = Unit

        override fun onError(e: Throwable) {
            Timber.d("User sign up failed with: %s", e.message)

            view!!.hideLoading()

            if (e is HttpException && e.code() == 422) view!!.showEmailTakenToast()
            if (e is IOException) view!!.showConnectionTimeoutToast()
        }

    }

}
