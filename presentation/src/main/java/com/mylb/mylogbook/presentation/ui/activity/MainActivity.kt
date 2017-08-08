package com.mylb.mylogbook.presentation.ui.activity

import android.accounts.Account
import android.app.Fragment
import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.device.authenticator.Auth
import com.mylb.mylogbook.presentation.device.sync.SyncAdapter
import com.mylb.mylogbook.presentation.di.component.AndroidComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAndroidComponent
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
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

    private val mainFragment: BaseFragment
        get() = (fragmentManager.findFragmentById(mainFrameLayout.id) as BaseFragment)

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

    override fun onBackPressed() { if (mainFragment.onBackButtonPressed()) super.onBackPressed() }

    fun showBottomNavigation() { bottomNavigation.visibility = View.VISIBLE }

    fun hideBottomNavigation() { bottomNavigation.visibility = View.GONE }

    fun isGooglePlayServicesAvailable(): Boolean {
        val api = GoogleApiAvailability.getInstance()
        val status = api.isGooglePlayServicesAvailable(this)

        if (status != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(status)) {
                api.getErrorDialog(this, status, ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, {
                    onActivityResult(
                            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
                            ConnectionResult.CANCELED,
                            null
                    )
                }).show()
            }

            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mainFragment.onActivityResult(requestCode, resultCode, data)
    }

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

    fun selectBottomNavigationItem(id: Int) { bottomNavigation.selectedItemId = id }

    private fun startFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
                .replace(mainFrameLayout.id, fragment)
                .commit()
    }

    companion object : SimpleActivityCompanion(MainActivity::class)

}
