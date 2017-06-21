package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class AndroidModule(private val context: Context) {

    @Provides @PerAndroidComponent @ForAndroidComponent
    fun provideContext(): Context = context

    @Provides @PerAndroidComponent
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}
