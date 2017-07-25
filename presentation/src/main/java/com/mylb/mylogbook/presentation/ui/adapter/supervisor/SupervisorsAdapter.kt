package com.mylb.mylogbook.presentation.ui.adapter.supervisor

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.longClicks
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.util.supervisorDrawable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_supervisor.view.*
import javax.inject.Inject

@PerAndroidComponent
class SupervisorsAdapter<T : Supervisor> @Inject constructor(
        @ForApplication private val context: Context
) : RecyclerView.Adapter<SupervisorsAdapter.ViewHolder<T>>() {

    val listItemClicks = PublishSubject.create<ViewHolder<T>>()
    val listItemLongClicks = PublishSubject.create<ViewHolder<T>>()

    var supervisors = listOf<T>()
        set(supervisors) {
            field = supervisors
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = LayoutInflater.from(context).inflate(R.layout.row_supervisor, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val supervisor = supervisors[position]

        holder.bind(supervisor)
        holder.itemView.clicks().map { holder }.subscribe(listItemClicks)
        holder.itemView.longClicks().map { holder }.subscribe(listItemLongClicks)
    }

    fun remove(supervisors: List<T>) { this.supervisors = this.supervisors.minus(supervisors) }

    override fun getItemCount() = supervisors.size

    class ViewHolder<T : Supervisor>(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var supervisor: T

        fun bind(supervisor: T) {
            this.supervisor = supervisor
            itemView.nameTextView.text = supervisor.name
            itemView.avatarImageView.setImageDrawable(itemView.context.supervisorDrawable(
                    supervisor.fullGender, supervisor.isAccredited
            ))
        }

    }

}

