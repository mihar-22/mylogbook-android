package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.presentation.device.sync.SyncService
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.di.module.SyncModule
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import dagger.Component

@PerAndroidComponent @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(AndroidModule::class, SyncModule::class)
)
interface SyncComponent {

    fun inject(service: SyncService)

}
