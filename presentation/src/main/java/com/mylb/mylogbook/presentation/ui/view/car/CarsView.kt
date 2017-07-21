package com.mylb.mylogbook.presentation.ui.view.car

import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.presentation.ui.adapter.car.CarsAdapter.ViewHolder
import io.reactivex.Observable

interface CarsView<T : Car> {

    val listItemClicks: Observable<ViewHolder<T>>
    val listItemLongClicks: Observable<ViewHolder<T>>
    val fabClicks: Observable<Unit>
    var isMultiSelectionEnabled: Boolean

    fun renderList(cars: List<T>)
    fun startMultiSelection()
    fun highlightListItem(holder: ViewHolder<T>)
    fun removeHighlightOnListItem(holder: ViewHolder<T>)
    fun showCarsDeletedToast(numberOfCars: Int)
    fun navigateToEditCar(car: T?)
    fun removeCarsFromList(cars: List<T>)

}
