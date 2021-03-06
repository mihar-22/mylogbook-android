package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.mylb.mylogbook.data.cache.KotPrefSettingsCache
import com.mylb.mylogbook.data.cache.KotPrefProgressCache
import com.mylb.mylogbook.data.cache.KotPrefUserCache
import com.mylb.mylogbook.data.executor.JobExecutor
import com.mylb.mylogbook.domain.cache.SettingsCache
import com.mylb.mylogbook.domain.cache.ProgressCache
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.UIThread
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides @Singleton @ForApplication
    fun provideApplicationContext(): Context = application

    @Provides @Singleton
    fun provideThreadExecutor(executor: JobExecutor): ThreadExecutor = executor

    @Provides @Singleton
    fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread = uiThread

    @Provides @Singleton
    fun provideUserCache(@ForApplication context: Context): UserCache = KotPrefUserCache(context)

    @Provides @Singleton
    fun provideSettingsCache(@ForApplication context: Context): SettingsCache = KotPrefSettingsCache(context)

    @Provides @Singleton
    fun provideProgressCache(@ForApplication context: Context): ProgressCache = KotPrefProgressCache(context)

    @Provides @Singleton
    fun provideRxLocation(@ForApplication context: Context) = RxLocation(context)

}
