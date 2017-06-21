package com.mylb.mylogbook.presentation

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.component.DaggerApplicationComponent
import com.mylb.mylogbook.presentation.di.module.ApplicationModule
import com.mylb.mylogbook.presentation.di.module.DatabaseModule
import com.mylb.mylogbook.presentation.di.module.HttpModule
import com.squareup.leakcanary.LeakCanary
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class AndroidApplication : Application() {

    val component: ApplicationComponent
        get() = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .httpModule(HttpModule(BuildConfig.API_URL))
                .databaseModule(DatabaseModule())
                .build()

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        if (BuildConfig.DEBUG) LeakCanary.install(this)

        Timber.plant(Timber.DebugTree())
        Kotpref.init(this)
        JodaTimeAndroid.init(this)

        Timber.d("Application ready")
    }

}