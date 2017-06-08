package com.mylb.mylogbook.presentation

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.component.DaggerApplicationComponent
import com.mylb.mylogbook.presentation.di.module.ApplicationModule
import com.mylb.mylogbook.presentation.di.module.HttpModule
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class AndroidApplication : Application() {

    val component: ApplicationComponent
        get() = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .httpModule(HttpModule(BuildConfig.API_URL))
                .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) { LeakCanary.install(this) }
        Kotpref.init(applicationContext)
        Timber.plant(Timber.DebugTree())

        Timber.d("Application ready")
    }
}