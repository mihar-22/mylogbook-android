package com.mylb.mylogbook.presentation.device.authenticator

import android.os.IBinder
import com.mylb.mylogbook.presentation.device.BaseService
import com.mylb.mylogbook.presentation.di.component.AuthComponent
import com.mylb.mylogbook.presentation.di.component.DaggerAuthComponent
import javax.inject.Inject

class AuthenticatorService : BaseService() {

    @Inject lateinit var authenticator: Authenticator

    override val binder: IBinder
        get() = authenticator.iBinder

    private val component: AuthComponent
        get() = DaggerAuthComponent.builder()
                .applicationComponent(applicationComponent)
                .androidModule(serviceModule)
                .build()

    override fun onCreate() {
        super.onCreate()

        component.inject(this)
    }

}
