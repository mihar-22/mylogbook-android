package com.mylb.mylogbook.presentation.ui.fragment.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.presenter.car.CarsPresenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import javax.inject.Inject

class CarsFragment : BaseFragment() {

    override val presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity.title = getString(R.string.bottom_nav_cars)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_cars, container, false)!!

    companion object Builder {

        fun build() = CarsFragment()

    }

}
