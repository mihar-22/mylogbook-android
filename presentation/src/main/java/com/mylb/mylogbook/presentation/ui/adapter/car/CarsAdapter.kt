package com.mylb.mylogbook.presentation.ui.adapter.car

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.longClicks
import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.util.carDrawable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_car.view.*
import javax.inject.Inject

@PerAndroidComponent
class CarsAdapter<T : Car> @Inject constructor(
        @ForApplication private val context: Context
) : RecyclerView.Adapter<CarsAdapter.ViewHolder<T>>() {

    val listItemClicks = PublishSubject.create<ViewHolder<T>>()
    val listItemLongClicks = PublishSubject.create<ViewHolder<T>>()

    var cars = listOf<T>()
        set(cars) {
            field = cars
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = LayoutInflater.from(context).inflate(R.layout.row_car, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val car = cars[position]

        holder.bind(car)
        holder.itemView.clicks().map { holder }.subscribe(listItemClicks)
        holder.itemView.longClicks().map { holder }.subscribe(listItemLongClicks)
    }

    fun remove(cars: List<T>) { this.cars = this.cars.minus(cars) }

    override fun getItemCount() = cars.size

    class ViewHolder<T : Car>(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var car: T

        fun bind(car: T) {
            this.car = car
            itemView.nameTextView.text = car.name
            itemView.typeImageView.setImageDrawable(itemView.context.carDrawable(car.type))
        }

    }

}
