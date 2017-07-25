package com.mylb.mylogbook.presentation.ui.fragment.supervisor

import android.graphics.Color
import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.supervisor.SupervisorsPresenter
import com.mylb.mylogbook.presentation.ui.adapter.supervisor.SupervisorsAdapter
import com.mylb.mylogbook.presentation.ui.adapter.supervisor.SupervisorsAdapter.ViewHolder
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.view.supervisor.SupervisorsView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_supervisors.*
import timber.log.Timber
import javax.inject.Inject

class SupervisorsFragment : BaseFragment(), SupervisorsView<Supervisor> {

    @Inject override lateinit var presenter: SupervisorsPresenter<Supervisor>
    @Inject lateinit var adapter: SupervisorsAdapter<Supervisor>

    override val listItemClicks: Observable<SupervisorsAdapter.ViewHolder<Supervisor>>
        get() = adapter.listItemClicks

    override val listItemLongClicks: Observable<SupervisorsAdapter.ViewHolder<Supervisor>>
        get() = adapter.listItemLongClicks

    override val fabClicks: Observable<Unit>
        get() = fab.clicks()

    override var isMultiSelectionEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.bottom_nav_supervisors)

        return inflater.inflate(R.layout.fragment_supervisors, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        presenter.view = this
        presenter.loadSupervisors()

        showBottomNavigation()
    }

    override fun renderList(supervisors: List<Supervisor>) {
        Timber.d("Rendering list of supervisors")

        adapter.supervisors = supervisors
    }

    override fun startMultiSelection() {
        isMultiSelectionEnabled = true
        hideFab()
        hideBottomNavigation()

        appCompatActivity.startSupportActionMode(object : ActionMode.Callback {

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                activity.menuInflater.inflate(R.menu.menu_list_cab, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = true

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                presenter.deleteMultiSelections()
                mode.finish()
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                isMultiSelectionEnabled = false
                presenter.endMultiSelection()
                showBottomNavigation()
                showFab()
            }

        })
    }

    fun showFab() { fab.visibility = View.VISIBLE}

    fun hideFab() { fab.visibility = View.GONE }

    override fun highlightListItem(holder: ViewHolder<Supervisor>) {
        holder.itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun removeHighlightOnListItem(holder: ViewHolder<Supervisor>) {
        holder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun removeSupervisorsFromList(supervisors: List<Supervisor>) { adapter.remove(supervisors) }

    override fun showSupervisorsDeletedToast(numberOfSupervisors: Int) {
        val message = activity
                .resources
                .getQuantityString(R.plurals.supervisors_deleted, numberOfSupervisors, numberOfSupervisors)

        showToast(message)
    }

    private fun setupRecyclerView() {
        supervisorsRecyclerView.layoutManager = LinearLayoutManager(activity)
        supervisorsRecyclerView.adapter = adapter
    }

    override fun navigateToEditSupervisor(supervisor: Supervisor?) {
        Timber.d("Navigating to edit supervisor (id: %d)", supervisor?.id)

        EditSupervisorFragment.start(this) { it.supervisor = supervisor }
    }

    companion object Builder {

        fun build() = SupervisorsFragment()

    }

}