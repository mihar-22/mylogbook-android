package com.mylb.mylogbook.presentation.presenter.car

import com.mylb.mylogbook.domain.interactor.car.DeleteCars
import com.mylb.mylogbook.domain.interactor.car.ShowCars
import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.adapter.car.CarsAdapter.ViewHolder
import com.mylb.mylogbook.presentation.ui.view.car.CarsView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class CarsPresenter<T : Car> @Inject constructor(
        private val showCars: ShowCars<T>,
        private val deleteCars: DeleteCars<T>,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: CarsView<T>? = null
        set(view) {
            field = view

            attachView()
        }

    private val multiSelections = arrayListOf<ViewHolder<T>>()

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
            observeListItemClicks()
            observeListItemLongClicks()
            observeMultiSelectionItemClicks()
            observeFabClicks()
        }
    }

    fun loadCars() { showCars.execute(ShowCarsObserver(), Unit) }

    private fun observeListItemClicks() {
        val itemClicks = view!!
                .listItemClicks
                .filter { !(view!!.isMultiSelectionEnabled) }
                .subscribe {
                    Timber.d("Clicked car (position: %d)", it.position)

                    view!!.navigateToEditCar(it.car)
                }

        disposables.add(itemClicks)
    }

    private fun observeListItemLongClicks() {
        val itemLongClicks = view!!
                .listItemLongClicks
                .filter { !(view!!.isMultiSelectionEnabled) }
                .subscribe {
                    Timber.d("Long clicked car (position: %d)", it.position)

                    view!!.startMultiSelection()
                    multiSelections.add(it)
                    view!!.highlightListItem(it)
                }

        disposables.addAll(itemLongClicks)
    }

    private fun observeMultiSelectionItemClicks() {
        val multiSelectionItemClicks = view!!
                .listItemClicks
                .filter { view!!.isMultiSelectionEnabled }
                .subscribe {
                    if(multiSelections.contains(it)) {
                        Timber.d("Removed car from multi selection (position: %d)", it.position)

                        multiSelections.remove(it)
                        view!!.removeHighlightOnListItem(it)
                    } else {
                        Timber.d("Added car to multi selection (position: %d)", it.position)

                        multiSelections.add(it)
                        view!!.highlightListItem(it)
                    }
                }

        disposables.add(multiSelectionItemClicks)
    }

    private fun observeFabClicks() {
        val fabClicks = view!!.fabClicks.subscribe { view!!.navigateToEditCar(null) }

        disposables.add(fabClicks)
    }

    fun deleteMultiSelections() {
        val cars = multiSelections.map { it.car }

        Timber.d("Deleting %d cars", cars.size)

        view!!.removeCarsFromList(cars)
        deleteCars.execute(DeleteCarsObserver(cars.size), cars)
    }

    fun endMultiSelection() {
        Timber.d("Ended multi selection")

        multiSelections.forEach { view!!.removeHighlightOnListItem(it) }
        multiSelections.clear()
    }

    private inner class ShowCarsObserver : DisposableObserver<List<T>>() {

        override fun onError(e: Throwable) { Timber.d("Loading cars failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(cars: List<T>) { view!!.renderList(cars) }

    }

    private inner class DeleteCarsObserver(val numberOfCars: Int) : DisposableObserver<Unit>() {

        override fun onError(e: Throwable) { Timber.d("Deleting cars failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(t: Unit) { view!!.showCarsDeletedToast(numberOfCars) }

    }

}
