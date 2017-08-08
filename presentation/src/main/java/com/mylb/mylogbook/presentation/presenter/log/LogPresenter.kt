package com.mylb.mylogbook.presentation.presenter.log

import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.interactor.car.ShowCars
import com.mylb.mylogbook.domain.interactor.supervisor.ShowSupervisors
import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.view.log.LogView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerAndroidComponent
class LogPresenter<C : Car, S : Supervisor> @Inject constructor(
        private val showCars: ShowCars<C>,
        private val showSupervisors: ShowSupervisors<S>,
        private val disposables: CompositeDisposable,
        private val postExecutionThread: PostExecutionThread
) : Presenter {

    var view: LogView<C, S>? = null
        set(view) {
            field = view

            attachView()
        }

    private var cars = listOf<C>()
    private var supervisors = listOf<S>()
    private var currentCar = 0
    private var currentSupervisor = 0

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
            observeOdometerChanges()
            observeSelections()
            observeNextButtonClicks()
        }
    }

    fun load() {
        showCars.execute(ShowCarsObserver(), Unit)
        showSupervisors.execute(ShowSupervisorsObserver(), Unit)
    }

    private fun observeFormValidationChanges() {
        val formValidationChanges = view!!.formValidationChanges.subscribe { view!!.enableNextButton(it) }

        disposables.add(formValidationChanges)
    }

    private fun observeOdometerChanges() {
        val odometerTextChanges = view!!
                .odometerTextChanges
                .filter { !(view!!.formattingInProgress) }
                .debounce(50, TimeUnit.MILLISECONDS)
                .observeOn(postExecutionThread.scheduler)
                .subscribe { view!!.formatOdometerText(it) }

        disposables.add(odometerTextChanges)
    }

    private fun observeSelections() {
        val carSelections = view!!.carSelections.subscribe {
            currentCar = it
            view!!.setOdometerForCar(it)
        }

        val supervisorSelections = view!!.supervisorSelections.subscribe { currentSupervisor = it }

        disposables.add(carSelections)
        disposables.add(supervisorSelections)
    }

    private fun observeNextButtonClicks() {
        val nextButtonClicks = view!!.nextButtonClicks.subscribe {
            view!!.navigateToRecording(cars[currentCar], supervisors[currentSupervisor])
        }

        disposables.add(nextButtonClicks)
    }

    private inner class ShowCarsObserver : DisposableObserver<List<C>>() {

        override fun onError(e: Throwable) { Timber.d("Loading cars failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(cars: List<C>) {
            this@LogPresenter.cars = cars
            view!!.renderCarsSpinner(cars)
        }

    }

    private inner class ShowSupervisorsObserver : DisposableObserver<List<S>>() {

        override fun onError(e: Throwable) { Timber.d("Loading supervisors failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(supervisors: List<S>) {
            this@LogPresenter.supervisors = supervisors
            view!!.renderSupervisorsSpinner(supervisors)
        }

    }


}
