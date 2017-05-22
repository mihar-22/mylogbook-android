package com.mylb.mylogbook.presentation.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.Navigator
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.module.ActivityModule
import okhttp3.OkHttpClient
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject lateinit var navigator: Navigator

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Inject lateinit var httpClient: OkHttpClient

    val applicationComponent: ApplicationComponent
        get() = (application as AndroidApplication).component

    val activityModule: ActivityModule
        get() = ActivityModule(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent.inject(this)
    }
}