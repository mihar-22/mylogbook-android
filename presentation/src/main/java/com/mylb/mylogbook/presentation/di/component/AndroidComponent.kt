package com.mylb.mylogbook.presentation.di.component

import android.content.Context
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import com.mylb.mylogbook.presentation.ui.fragment.car.CarsFragment
import com.mylb.mylogbook.presentation.ui.fragment.car.EditCarFragment
import com.mylb.mylogbook.presentation.ui.fragment.supervisor.EditSupervisorFragment
import com.mylb.mylogbook.presentation.ui.fragment.supervisor.SupervisorsFragment
import dagger.Component

@PerAndroidComponent @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(AndroidModule::class)
)
interface AndroidComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: CarsFragment)
    fun inject(fragment: EditCarFragment)
    fun inject(fragment: SupervisorsFragment)
    fun inject(fragment: EditSupervisorFragment)

    // Android Module
    @ForAndroidComponent fun context(): Context

}
