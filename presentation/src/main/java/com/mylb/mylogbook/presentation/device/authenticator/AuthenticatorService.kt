package com.mylb.mylogbook.presentation.device.authenticator

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class AuthenticatorService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        Timber.d("Binding service")

        val authenticator = Authenticator(this)

        return authenticator.iBinder
    }
}
