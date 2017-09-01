package com.mylb.mylogbook.presentation.presenter.setup

import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.setup.SetupStateView
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class SetupStatePresenter @Inject constructor(
        private val disposables: CompositeDisposable
) : Presenter {

    var view: SetupStateView? = null
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

        if (view != null) { observeSubmitButtonClicks() }
    }

    private fun observeSubmitButtonClicks() {
        val submitButtonClicks = view!!.submitButtonClicks.subscribe {
            view!!.navigateToDashboard()
        }

        disposables.add(submitButtonClicks)
    }

}
