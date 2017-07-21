package com.mylb.mylogbook.presentation.ui.activity

import android.accounts.Account
import android.app.Fragment
import android.content.ContentResolver
import android.os.Bundle
import android.view.View
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.device.authenticator.Auth
import com.mylb.mylogbook.presentation.device.sync.SyncAdapter
import com.mylb.mylogbook.presentation.di.component.AndroidComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.fragment.car.CarsFragment
import com.mylb.mylogbook.presentation.ui.fragment.dashboard.DashboardFragment
import com.mylb.mylogbook.presentation.ui.fragment.log.LogFragment
import com.mylb.mylogbook.presentation.ui.fragment.supervisor.SupervisorsFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.eugeniomarletti.extras.SimpleActivityCompanion
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var userCache: UserCache

    override val presenter: Presenter? = null

    private val component: AndroidComponent
        get() = DaggerAndroidComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(activityModule)
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component.inject(this)

        onBottomNavigationItemSelection()

        if (savedInstanceState != null) return

        startFragment(DashboardFragment.build())

        ContentResolver.addPeriodicSync(
                Account(userCache.email, Auth.ACCOUNT_TYPE),
                SyncAdapter.AUTHORITY,
                Bundle.EMPTY,
                SyncAdapter.SYNC_INTERVAL
        )
    }

    fun showBottomNavigation() { bottomNavigation.visibility = View.VISIBLE }

    fun hideBottomNavigation() { bottomNavigation.visibility = View.GONE }

    private fun onBottomNavigationItemSelection() =
            bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.dashboardMenuItem -> startFragment(DashboardFragment.build())
                    R.id.carsMenuItem -> startFragment(CarsFragment.build())
                    R.id.supervisorsMenuItem -> startFragment(SupervisorsFragment.build())
                    R.id.logMenuItem -> startFragment(LogFragment.build())
                }

                return@setOnNavigationItemSelectedListener true
            }

    private fun startFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .replace(mainFrameLayout.id, fragment)
                .commit()
    }

    companion object : SimpleActivityCompanion(MainActivity::class)

}
