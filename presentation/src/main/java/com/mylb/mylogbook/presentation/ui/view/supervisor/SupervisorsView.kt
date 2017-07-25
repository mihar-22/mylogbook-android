package com.mylb.mylogbook.presentation.ui.view.supervisor

import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.ui.adapter.supervisor.SupervisorsAdapter
import io.reactivex.Observable

interface SupervisorsView<T : Supervisor> {

    val listItemClicks: Observable<SupervisorsAdapter.ViewHolder<T>>
    val listItemLongClicks: Observable<SupervisorsAdapter.ViewHolder<T>>
    val fabClicks: Observable<Unit>
    var isMultiSelectionEnabled: Boolean

    fun renderList(supervisors: List<T>)
    fun startMultiSelection()
    fun highlightListItem(holder: SupervisorsAdapter.ViewHolder<T>)
    fun removeHighlightOnListItem(holder: SupervisorsAdapter.ViewHolder<T>)
    fun showSupervisorsDeletedToast(numberOfSupervisors: Int)
    fun navigateToEditSupervisor(supervisor: T?)
    fun removeSupervisorsFromList(supervisors: List<T>)

}

