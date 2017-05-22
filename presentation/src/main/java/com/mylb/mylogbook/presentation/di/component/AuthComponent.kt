package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.domain.repository.AuthRepository
import com.mylb.mylogbook.presentation.di.module.ActivityModule
import com.mylb.mylogbook.presentation.di.module.AuthModule
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import dagger.Component

@PerActivity @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ActivityModule::class, AuthModule::class)
)
interface AuthComponent : ActivityComponent {
    fun inject(activity: SignUpActivity)
}