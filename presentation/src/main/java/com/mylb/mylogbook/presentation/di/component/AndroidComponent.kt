package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import dagger.Component

@PerAndroidComponent @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(AndroidModule::class)
)
interface AndroidComponent {

    fun inject(activity: MainActivity)

}
