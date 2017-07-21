package com.mylb.mylogbook.presentation.presenter.car

import com.mylb.mylogbook.domain.interactor.car.SaveCar
import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.car.EditCarView
import com.mylb.mylogbook.presentation.ui.view.car.EditCarView.FormField.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class EditCarPresenter<T : Car> @Inject constructor(
        private val saveCar: SaveCar<T>,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: EditCarView<T>? = null
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
            observerTypeSelections()
        }
    }

    private fun observeFormValidationChanges() {
        val formValidationChanges = view!!.formValidationChanges.subscribe {
            view!!.enableSubmitButton(it)
        }

        disposables.add(formValidationChanges)
    }

    private fun observerTypeSelections() {
        val typeSelections = view!!.typeSelections.subscribe { view!!.selectType(it) }

        disposables.add(typeSelections)
    }

    fun onSubmitButtonClick(car: T) {
        car.name = view!!.text(NAME)
        car.registration = view!!.text(REGISTRATION)
        car.type = view!!.text(TYPE)

        saveCar.execute(SaveCarObserver(), car)
    }

    private inner class SaveCarObserver : DisposableObserver<Unit>() {

        override fun onError(e: Throwable) { Timber.d("Failed to save car with: %s", e.message) }

        override fun onNext(t: Unit) {
            Timber.d("Saved car")

            view!!.finishEditing()
        }

        override fun onComplete() = Unit

    }

}