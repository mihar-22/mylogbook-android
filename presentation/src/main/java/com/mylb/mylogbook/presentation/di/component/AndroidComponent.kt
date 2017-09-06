package com.mylb.mylogbook.presentation.di.component

import android.content.Context
import com.mylb.mylogbook.presentation.di.module.AndroidModule
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import com.mylb.mylogbook.presentation.ui.activity.settings.SettingsActivity
import com.mylb.mylogbook.presentation.ui.activity.setup.SetupLicenseActivity
import com.mylb.mylogbook.presentation.ui.activity.setup.SetupStateActivity
import com.mylb.mylogbook.presentation.ui.fragment.car.CarsFragment
import com.mylb.mylogbook.presentation.ui.fragment.car.EditCarFragment
import com.mylb.mylogbook.presentation.ui.fragment.log.LogDetailsFragment
import com.mylb.mylogbook.presentation.ui.fragment.log.LogFragment
import com.mylb.mylogbook.presentation.ui.fragment.log.RecordLogFragment
import com.mylb.mylogbook.presentation.ui.fragment.supervisor.EditSupervisorFragment
import com.mylb.mylogbook.presentation.ui.fragment.supervisor.SupervisorsFragment
import dagger.Component

@PerAndroidComponent @Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(AndroidModule::class)
)
interface AndroidComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: SetupLicenseActivity)
    fun inject(activity: SetupStateActivity)
    fun inject(activity: SettingsActivity)

    fun inject(fragment: CarsFragment)
    fun inject(fragment: EditCarFragment)
    fun inject(fragment: SupervisorsFragment)
    fun inject(fragment: EditSupervisorFragment)
    fun inject(fragment: LogFragment)
    fun inject(fragment: RecordLogFragment)
    fun inject(fragment: LogDetailsFragment)

    // Android Module
    @ForAndroidComponent fun context(): Context

}
