package com.mylb.mylogbook.presentation.presenter.supervisor

import com.mylb.mylogbook.domain.interactor.supervisor.SaveSupervisor
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.supervisor.EditSupervisorView
import com.mylb.mylogbook.presentation.ui.view.supervisor.EditSupervisorView.FormField.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class EditSupervisorPresenter<T : Supervisor> @Inject constructor(
        private val saveSupervisor: SaveSupervisor<T>,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: EditSupervisorView<T>? = null
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
            observeGenderSelections()
            observeAccreditedCheckedChanges()
        }
    }

    private fun observeFormValidationChanges() {
        val formValidationChanges = view!!.formValidationChanges.subscribe {
            view!!.enableSubmitButton(it)
        }

        disposables.add(formValidationChanges)
    }

    private fun observeGenderSelections() {
        val maleCheckedChanges = view!!.maleCheckedChanges.subscribe { view!!.selectGender(it) }
        val femaleCheckedChanges = view!!.femaleCheckedChanges.subscribe { view!!.selectGender(!it) }

        disposables.add(maleCheckedChanges)
        disposables.add(femaleCheckedChanges)
    }

    private fun observeAccreditedCheckedChanges() {
        val accreditedCheckedChanges = view!!.accreditedCheckedChanges.subscribe {
            view!!.checkIsAccredited(it)
        }

        disposables.add(accreditedCheckedChanges)
    }

    fun onSubmitButtonClick(supervisor: T) {
        supervisor.name = view!!.value(NAME) as String
        supervisor.gender = view!!.value(GENDER) as String
        supervisor.isAccredited = view!!.value(IS_ACCREDITED) as Boolean

        saveSupervisor.execute(SaveSupervisorObserver(), supervisor)
    }

    private inner class SaveSupervisorObserver : DisposableObserver<Unit>() {

        override fun onError(e: Throwable) { Timber.d("Failed to save supervisor with: %s", e.message) }

        override fun onNext(t: Unit) {
            Timber.d("Saved supervisor")

            view!!.finishEditing()
        }

        override fun onComplete() = Unit

    }


}
