package com.mylb.mylogbook.presentation.ui.activity

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.presenter.Presenter
import okhttp3.OkHttpClient
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Inject lateinit var httpClient: OkHttpClient

    abstract val presenter: Presenter?

    protected val applicationComponent: ApplicationComponent
        get() = (application as AndroidApplication).component

    protected val activityModule: AndroidModule
        get() = AndroidModule(this)

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

    protected fun showToast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}