package com.mylb.mylogbook.presentation.ui.fragment.dashboard

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import com.github.lzyzsd.circleprogress.ArcProgress
import com.mylb.mylogbook.presentation.R
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.settings.SettingsActivity
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.mylb.mylogbook.presentation.ui.fragment.util.SimpleFragmentCompanion
import kotlinx.android.synthetic.main.fragment_dashboard.*
import timber.log.Timber

class DashboardFragment : BaseFragment() {

    override val presenter: Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        showBottomNavigation()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?
    ): View {
        activity.title = getString(R.string.bottom_nav_dashboard)

        return inflater.inflate(R.layout.fragment_dashboard, container, false)!!
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exportMenuItem) {
            Timber.d("Exporting")

            // export
        }

        if (item.itemId == R.id.settingsMenuItem) {
            Timber.d("Navigating to settings")

            SettingsActivity.start(activity)
        }

        return true
    }

    override fun onBackButtonPressed(): Boolean {
        moveTaskToBack()

        return false
    }

    companion object Builder : SimpleFragmentCompanion(DashboardFragment::class) {

        fun build() = DashboardFragment()

    }

}