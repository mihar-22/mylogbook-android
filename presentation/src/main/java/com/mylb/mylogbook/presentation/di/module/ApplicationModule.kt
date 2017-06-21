package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.mylb.mylogbook.data.cache.KotPrefUserCache
import com.mylb.mylogbook.data.executor.JobExecutor
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.presentation.AndroidApplication
import com.mylb.mylogbook.presentation.UIThread
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
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
    fun provideUserCache(cache: KotPrefUserCache): UserCache = cache

}
