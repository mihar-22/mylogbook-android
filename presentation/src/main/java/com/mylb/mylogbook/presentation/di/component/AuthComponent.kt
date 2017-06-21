package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.presentation.device.authenticator.AuthenticatorService
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.di.module.AuthModule
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.ui.activity.LaunchActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.LogInActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import dagger.Component

@PerAndroidComponent @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(AndroidModule::class, AuthModule::class)
)
interface AuthComponent {

    fun inject(activity: LaunchActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: LogInActivity)
    fun inject(service: AuthenticatorService)

}
