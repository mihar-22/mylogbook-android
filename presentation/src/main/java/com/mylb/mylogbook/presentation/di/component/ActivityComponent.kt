package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.presentation.di.module.ActivityModule
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import dagger.Component

@PerActivity @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent
