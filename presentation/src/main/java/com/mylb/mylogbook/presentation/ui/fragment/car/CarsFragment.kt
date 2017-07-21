package com.mylb.mylogbook.presentation.ui.fragment.car

import android.graphics.Color
import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.jakewharton.rxbinding2.view.clicks
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.car.CarsPresenter
import com.mylb.mylogbook.presentation.ui.adapter.car.CarsAdapter
import com.mylb.mylogbook.presentation.ui.adapter.car.CarsAdapter.ViewHolder
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.view.car.CarsView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_cars.*
import timber.log.Timber
import javax.inject.Inject

class CarsFragment : BaseFragment(), CarsView<Car> {

    @Inject override lateinit var presenter: CarsPresenter<Car>
    @Inject lateinit var adapter: CarsAdapter<Car>

    override val listItemClicks: Observable<ViewHolder<Car>>
        get() = adapter.listItemClicks

    override val listItemLongClicks: Observable<ViewHolder<Car>>
        get() = adapter.listItemLongClicks

    override val fabClicks: Observable<Unit>
        get() = fab.clicks()

    override var isMultiSelectionEnabled = false

    init { retainInstance = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.bottom_nav_cars)

        return inflater.inflate(R.layout.fragment_cars, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        presenter.view = this
        presenter.loadCars()

        showBottomNavigation()
    }

    override fun renderList(cars: List<Car>) {
        Timber.d("Rendering list of cars")

        adapter.cars = cars
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

    override fun highlightListItem(holder: ViewHolder<Car>) {
        holder.itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun removeHighlightOnListItem(holder: ViewHolder<Car>) {
        holder.itemView.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun removeCarsFromList(cars: List<Car>) { adapter.remove(cars) }

    override fun showCarsDeletedToast(numberOfCars: Int) {
        val message = activity
                .resources
                .getQuantityString(R.plurals.cars_deleted, numberOfCars, numberOfCars)

        showToast(message)
    }

    private fun setupRecyclerView() {
        carsRecyclerView.layoutManager = LinearLayoutManager(activity)
        carsRecyclerView.adapter = adapter
    }

    override fun navigateToEditCar(car: Car?) {
        Timber.d("Navigating to edit car (id: %d)", car?.id)

        EditCarFragment.start(this) { it.car = car }
    }

    companion object Builder { fun build() = CarsFragment() }

}