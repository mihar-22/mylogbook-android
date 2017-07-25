package com.mylb.mylogbook.presentation.presenter.supervisor

import com.mylb.mylogbook.domain.interactor.supervisor.DeleteSupervisors
import com.mylb.mylogbook.domain.interactor.supervisor.ShowSupervisors
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.adapter.supervisor.SupervisorsAdapter.ViewHolder
import com.mylb.mylogbook.presentation.ui.view.supervisor.SupervisorsView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

@PerAndroidComponent
class SupervisorsPresenter<T : Supervisor> @Inject constructor(
        private val showSupervisors: ShowSupervisors<T>,
        private val deleteSupervisors: DeleteSupervisors<T>,
        private val disposables: CompositeDisposable
) : Presenter {

    var view: SupervisorsView<T>? = null
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

    fun loadSupervisors() { showSupervisors.execute(ShowSupervisorsObserver(), Unit) }

    private fun observeListItemClicks() {
        val itemClicks = view!!
                .listItemClicks
                .filter { !(view!!.isMultiSelectionEnabled) }
                .subscribe {
                    Timber.d("Clicked supervisor (position: %d)", it.position)

                    view!!.navigateToEditSupervisor(it.supervisor)
                }

        disposables.add(itemClicks)
    }

    private fun observeListItemLongClicks() {
        val itemLongClicks = view!!
                .listItemLongClicks
                .filter { !(view!!.isMultiSelectionEnabled) }
                .subscribe {
                    Timber.d("Long clicked supervisor (position: %d)", it.position)

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
                        Timber.d("Removed supervisor from multi selection (position: %d)", it.position)

                        multiSelections.remove(it)
                        view!!.removeHighlightOnListItem(it)
                    } else {
                        Timber.d("Added supervisor to multi selection (position: %d)", it.position)

                        multiSelections.add(it)
                        view!!.highlightListItem(it)
                    }
                }

        disposables.add(multiSelectionItemClicks)
    }

    private fun observeFabClicks() {
        val fabClicks = view!!.fabClicks.subscribe { view!!.navigateToEditSupervisor(null) }

        disposables.add(fabClicks)
    }

    fun deleteMultiSelections() {
        val supervisors = multiSelections.map { it.supervisor }

        Timber.d("Deleting %d supervisors", supervisors.size)

        view!!.removeSupervisorsFromList(supervisors)
        deleteSupervisors.execute(DeleteSupervisorsObserver(supervisors.size), supervisors)
    }

    fun endMultiSelection() {
        Timber.d("Ended multi selection")

        multiSelections.forEach { view!!.removeHighlightOnListItem(it) }
        multiSelections.clear()
    }


    private inner class ShowSupervisorsObserver : DisposableObserver<List<T>>() {

        override fun onError(e: Throwable) { Timber.d("Loading supervisors failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(supervisors: List<T>) { view!!.renderList(supervisors) }

    }

    private inner class DeleteSupervisorsObserver(val numberOfSupervisors: Int) : DisposableObserver<Unit>() {

        override fun onError(e: Throwable) { Timber.d("Deleting supervisors failed with: %s", e.message) }

        override fun onComplete() = Unit

        override fun onNext(t: Unit) { view!!.showSupervisorsDeletedToast(numberOfSupervisors) }

    }

}
