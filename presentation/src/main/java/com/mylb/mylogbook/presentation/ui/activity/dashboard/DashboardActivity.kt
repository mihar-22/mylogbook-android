package com.mylb.mylogbook.presentation.ui.activity.dashboard

import android.os.Bundle
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.view.dashboard.DashboardView
import kotlinx.android.synthetic.main.activity_dashboard.*
import me.eugeniomarletti.extras.SimpleActivityCompanion

@PerActivity
class DashboardActivity : BaseActivity(), DashboardView {

    override val presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }

    override fun onBackPressed() { moveTaskToBack(true) }

    companion object : SimpleActivityCompanion(DashboardActivity::class)
}
