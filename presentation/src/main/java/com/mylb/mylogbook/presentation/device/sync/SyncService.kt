package com.mylb.mylogbook.presentation.device.sync

import android.os.IBinder
import com.mylb.mylogbook.presentation.device.BaseService
import com.mylb.mylogbook.presentation.di.component.DaggerSyncComponent
import com.mylb.mylogbook.presentation.di.component.SyncComponent
import javax.inject.Inject

class SyncService : BaseService() {

    @Inject lateinit var syncAdapter: SyncAdapter

    private val syncLock = Object()

    override val binder: IBinder
        get() = syncAdapter.syncAdapterBinder

    private val component: SyncComponent
        get() = DaggerSyncComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(serviceModule)
                .build()

    override fun onCreate() {
        super.onCreate()

        synchronized(syncLock) { component.inject(this) }
    }

}
