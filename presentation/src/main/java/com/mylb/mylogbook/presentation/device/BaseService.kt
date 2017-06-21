package com.mylb.mylogbook.presentation.device

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.di.component.ApplicationComponent
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import timber.log.Timber

abstract class BaseService : Service() {

    abstract val binder: IBinder

    protected val applicationComponent: ApplicationComponent
        get() = (application as AndroidApplication).component

    protected val serviceModule: AndroidModule
        get() = AndroidModule(this)

    override fun onCreate() {
        Timber.d("Creating service")

        super.onCreate()

        applicationComponent.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        Timber.d("Binding service")

        return binder
    }

}
