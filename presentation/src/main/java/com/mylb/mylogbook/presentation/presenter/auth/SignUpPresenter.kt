package com.mylb.mylogbook.presentation.presenter.auth

import android.util.Log
import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.interactor.auth.SignUpUser
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.auth.SignUpView
import com.mylb.mylogbook.presentation.validation.ValidationRule
import com.mylb.mylogbook.presentation.validation.Validator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@PerActivity
class SignUpPresenter @Inject constructor(
        private val useCase: SignUpUser,
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
        disposables.dispose()

        view = null
    }

    fun attachView() {
        if (view == null) {
            disposables.clear()

            return
        }

        observeFormChanges()
        observeSubmitButtonClicks()
    }

    fun validationRules(field: SignUpView.Field): ArrayList<ValidationRule> {
        val rules = ArrayList<ValidationRule>()

        rules.add(Validator.Required())

        when (field) {
            SignUpView.Field.NAME -> { rules.add(Validator.MaxLength(100)) }
            SignUpView.Field.EMAIL -> { rules.add(Validator.Email()) }
            SignUpView.Field.PASSWORD -> { rules.add(Validator.MinLength(6)) }
            SignUpView.Field.BIRTHDATE -> { rules.add(Validator.Date()) }
        }

        return rules
    }

    fun observeFormChanges() {
        val formChanges = Validator.validationChanges(view!!, this::validationRules)
                .subscribe { isFormValid -> view!!.enableSubmitButton(isFormValid) }

        disposables.add(formChanges)
    }

    fun observeSubmitButtonClicks() {
        val submitButtonClicks = view!!.submitButtonClicks.subscribe {
            view!!.showLoading()

            useCase.execute(
                    SignUpUserObserver(),
                    SignUpUser.Companion.NewUser(
                            view!!.text(SignUpView.Field.NAME).toString(),
                            view!!.text(SignUpView.Field.EMAIL).toString(),
                            view!!.text(SignUpView.Field.PASSWORD).toString(),
                            view!!.text(SignUpView.Field.BIRTHDATE).toString()
                    )
            )
        }

        disposables.add(submitButtonClicks)
    }

    private inner class SignUpUserObserver : DisposableObserver<Response<Unit>>() {
        override fun onComplete() {}

        override fun onError(e: Throwable?) {
            view!!.hideLoading()

            if (e is HttpException) { view!!.showEmailTakenToast() }

            if (e is IOException) { view!!.showConnectionTimeoutToast() }
        }

        override fun onNext(t: Response<Unit>?) {
            view!!.hideLoading()

            view!!.showSignUpSuccessAlert()
        }
    }
}
