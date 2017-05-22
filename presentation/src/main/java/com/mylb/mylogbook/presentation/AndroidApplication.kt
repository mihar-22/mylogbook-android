package com.mylb.mylogbook.presentation

import android.app.Application
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.component.DaggerApplicationComponent
import com.mylb.mylogbook.presentation.di.module.ApplicationModule
import com.mylb.mylogbook.presentation.di.module.HttpModule
import com.squareup.leakcanary.LeakCanary

class AndroidApplication : Application() {
    val component: ApplicationComponent
        get() = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .httpModule(HttpModule(BuildConfig.API_URL))
                .build()

    override fun onCreate() {
        super.onCreate()
        initLeakDetector()
    }

    private fun initLeakDetector() { if (BuildConfig.DEBUG) { LeakCanary.install(this) } }
}