package com.mylb.mylogbook.presentation.test.espresso.resource

import android.support.test.espresso.IdlingResource.ResourceCallback
import okhttp3.OkHttpClient
import android.support.test.espresso.IdlingResource

class OkHttpResource constructor(client: OkHttpClient) : IdlingResource {

    private val dispatcher = client.dispatcher()

    @Volatile lateinit var callback: ResourceCallback

    init { dispatcher.setIdleCallback { callback.onTransitionToIdle() } }

    override fun getName(): String = "OkHttp"

    override fun isIdleNow(): Boolean = (dispatcher.runningCallsCount() == 0)

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }

}
