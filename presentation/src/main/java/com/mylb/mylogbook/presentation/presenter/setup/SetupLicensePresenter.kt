package com.mylb.mylogbook.presentation.presenter.setup

import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.setup.SetupLicenseView
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class SetupLicensePresenter @Inject constructor(
        private val disposables: CompositeDisposable
) : Presenter {

    var view: SetupLicenseView? = null
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
            view!!.navigateToStateSetup()
        }

        disposables.add(submitButtonClicks)
    }

}
