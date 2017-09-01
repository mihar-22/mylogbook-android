package com.mylb.mylogbook.presentation.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.di.component.AndroidComponent
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAndroidComponent
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.presenter.Presenter
import com.mylb.mylogbook.presentation.ui.activity.MainActivity

abstract class BaseFragment : Fragment() {

    abstract val presenter: Presenter?

    var intent
        get() = activity.intent
        set(intent) { activity.intent = intent }

    protected val mainActivity
        get() = (activity as MainActivity)

    protected val appCompatActivity
        get() = (activity as AppCompatActivity)

    protected val applicationComponent: ApplicationComponent
        get() = (activity.application as AndroidApplication).component

    protected val component: AndroidComponent
        get() = DaggerAndroidComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(AndroidModule(activity))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent.inject(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

    fun showBottomNavigation() = mainActivity.showBottomNavigation()

    fun hideBottomNavigation() = mainActivity.hideBottomNavigation()

    fun hideSoftKeyboard() = mainActivity.hideSoftKeyboard()

    fun moveTaskToBack() = mainActivity.moveTaskToBack(true)

    protected fun isGooglePlayServicesAvailable() = mainActivity.isGooglePlayServicesAvailable()

    protected fun showToast(message: CharSequence) = Toast.makeText(activity, message, Toast.LENGTH_LONG).show()

    protected fun popOffBackStack() { activity.fragmentManager.popBackStack() }

    protected fun selectBottomNavigationItem(id: Int) = mainActivity.selectBottomNavigationItem(id)

    protected fun clearBackStack() {
        for (i in 0 .. activity.fragmentManager.backStackEntryCount) {
            activity.fragmentManager.popBackStack()
        }
    }

    open fun onBackButtonPressed() = true

}