package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.mylb.mylogbook.presentation.di.qualifier.ForActivity
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides @PerActivity @ForActivity
    fun provideActivityContext(): Context = activity

    @Provides @PerActivity
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}
