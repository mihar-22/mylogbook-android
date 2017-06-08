package com.mylb.mylogbook.presentation.ui.activity

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.module.ActivityModule
import com.mylb.mylogbook.presentation.presenter.Presenter
import okhttp3.OkHttpClient
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Inject lateinit var httpClient: OkHttpClient

    @Inject lateinit var cache: UserCache

    abstract val presenter: Presenter?

    val applicationComponent: ApplicationComponent
        get() = (application as AndroidApplication).component

    val activityModule: ActivityModule
        get() = ActivityModule(this)

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
}