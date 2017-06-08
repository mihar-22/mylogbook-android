package com.mylb.mylogbook.presentation.presenter.auth

import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.interactor.auth.SignUpUser
import com.mylb.mylogbook.domain.interactor.auth.SignUpUser.Params.NewUser
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.Field.NAME
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.Field.EMAIL
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.Field.PASSWORD
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView.Field.BIRTHDATE
import com.mylb.mylogbook.presentation.validation.ValidatingView
import com.mylb.mylogbook.presentation.validation.ValidationRule
import com.mylb.mylogbook.presentation.validation.Validator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@PerActivity
class SignUpPresenter @Inject constructor(
        private val signUpUser: SignUpUser,
        private val disposables: CompositeDisposable
) : Presenter, ValidatingView.Presenter<SignUpView.Field> {

    var view: SignUpView? = null
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

    fun attachView() {
        if (view == null) {
            Timber.d("View is null")

            disposables.clear()
            return
        }

        observeValidationChanges()
        observeSubmitButtonClicks()
    }

    override fun validationRules(field: SignUpView.Field): ArrayList<ValidationRule> {
        val rules = ArrayList<ValidationRule>()

        rules.add(Validator.Required())

        when (field) {
            NAME -> rules.add(Validator.MaxLength(100))
            EMAIL -> rules.add(Validator.Email())
            PASSWORD -> rules.add(Validator.MinLength(6))
            BIRTHDATE -> rules.add(Validator.Date())
        }

        return rules
    }

    override fun observeValidationChanges() {
        Timber.d("Observing validation changes")

        val validationChanges = Validator.validationChanges(view!!, this::validationRules, { _ , _ -> })
                .subscribe { isFormValid -> view!!.enableSubmitButton(isFormValid) }

        disposables.add(validationChanges)
    }

    fun observeSubmitButtonClicks() {
        Timber.d("Observing submit button clicks")

        val submitButtonClicks = view!!.submitButtonClicks.subscribe {
            view!!.showLoading()

            signUpUser.execute(
                    SignUpUserObserver(),
                    NewUser(
                            view!!.text(NAME).toString(),
                            view!!.text(EMAIL).toString(),
                            view!!.text(PASSWORD).toString(),
                            view!!.text(BIRTHDATE).toString()
                    )
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

        override fun onError(e: Throwable?) {
            Timber.d("User sign up failed with: %s", e?.message)

            view!!.hideLoading()

            if (e is HttpException && e.code() == 422) view!!.showEmailTakenToast()
            if (e is IOException) view!!.showConnectionTimeoutToast()
        }
    }
}
