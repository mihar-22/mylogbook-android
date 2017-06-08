package com.mylb.mylogbook.presentation.di.component

import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.presentation.di.module.ApplicationModule
import com.mylb.mylogbook.presentation.di.module.HttpModule
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton @Component(modules = arrayOf(ApplicationModule::class, HttpModule::class))
interface ApplicationComponent {
    fun inject(activity: BaseActivity)

    // Application Module
    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread
    fun userCache(): UserCache

    // Http Module
    fun retrofit(): Retrofit
    fun okHttpClient(): OkHttpClient
}