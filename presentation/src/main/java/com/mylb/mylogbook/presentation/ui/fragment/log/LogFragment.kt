package com.mylb.mylogbook.presentation.ui.fragment.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment

class LogFragment : BaseFragment() {

    override val presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity.title = getString(R.string.bottom_nav_log)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_log, container, false)!!

    companion object Builder {

        fun build() = LogFragment()

    }

}