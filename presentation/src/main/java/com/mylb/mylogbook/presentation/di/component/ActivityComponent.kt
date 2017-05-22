package com.mylb.mylogbook.presentation.di.component

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.mylb.mylogbook.presentation.di.module.ActivityModule
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import com.mylb.mylogbook.presentation.ui.activity.auth.SignUpActivity
import dagger.Component

@PerActivity @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ActivityModule::class)
)
interface ActivityComponent
