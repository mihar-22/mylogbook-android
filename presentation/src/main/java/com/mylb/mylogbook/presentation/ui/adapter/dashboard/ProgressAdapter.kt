package com.mylb.mylogbook.presentation.ui.adapter.dashboard

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import javax.inject.Inject

@PerAndroidComponent
class ProgressAdapter @Inject constructor(
        @ForApplication private val context: Context
) : RecyclerView.Adapter<ProgressAdapter.ViewHolder>() {

    // list of tasks

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // lateinit var car: T

        // bind tasks
        fun bind() {}

    }

}
