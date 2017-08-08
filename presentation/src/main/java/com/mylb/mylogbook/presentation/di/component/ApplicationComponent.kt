package com.mylb.mylogbook.presentation.di.component

import android.content.Context
import com.mylb.mylogbook.data.database.Database
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteCarRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteSupervisorRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteTripRepository
import com.mylb.mylogbook.domain.executor.PostExecutionThread
import com.mylb.mylogbook.domain.executor.ThreadExecutor
import com.mylb.mylogbook.presentation.device.BaseService
import com.mylb.mylogbook.presentation.di.module.ApplicationModule
import com.mylb.mylogbook.presentation.di.module.DatabaseModule
import com.mylb.mylogbook.presentation.di.module.HttpModule
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.mylb.mylogbook.presentation.ui.activity.BaseActivity
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import com.patloew.rxlocation.RxLocation
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton @Component(modules = arrayOf(
        ApplicationModule::class, HttpModule::class, DatabaseModule::class
))
interface ApplicationComponent {

    fun inject(activity: BaseActivity)
    fun inject(service: BaseService)
    fun inject(fragment: BaseFragment)

    // Application Module
    @ForApplication fun context(): Context
    fun threadExecutor(): ThreadExecutor
    fun postExecutionThread(): PostExecutionThread
    fun userCache(): UserCache
    fun rxLocation(): RxLocation

    // Http Module
    fun retrofit(): Retrofit
    fun okHttpClient(): OkHttpClient
    fun remoteCarRepository(): RemoteCarRepository<Car>
    fun remoteSupervisorRepository(): RemoteSupervisorRepository<Supervisor>
    fun remoteTripRepository(): RemoteTripRepository<Trip>

    // Database Module
    fun database(): Database
    fun localCarRepository(): LocalCarRepository<Car>
    fun localSupervisorRepository(): LocalSupervisorRepository<Supervisor>
    fun localTripRepository(): LocalTripRepository<Trip>

}