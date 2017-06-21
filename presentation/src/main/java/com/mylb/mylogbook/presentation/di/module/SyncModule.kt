package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.sync.CarSync
import com.mylb.mylogbook.data.sync.SupervisorSync
import com.mylb.mylogbook.data.sync.TripSync
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteCarRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteSupervisorRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteTripRepository
import com.mylb.mylogbook.presentation.device.sync.SyncAdapter
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import dagger.Module
import dagger.Provides

@Module
class SyncModule {

    @Provides @PerAndroidComponent
    fun provideSyncAdapter(
            @ForAndroidComponent context: Context,
            userCache: UserCache,
            carSync: CarSync,
            supervisorSync: SupervisorSync,
            tripSync: TripSync
    ) = SyncAdapter(context, userCache, carSync, supervisorSync, tripSync)

    @Provides @PerAndroidComponent
    fun provideCarSync(
            userCache: UserCache,
            localRepository: LocalCarRepository<Car>,
            remoteRepository: RemoteCarRepository<Car>
    ) = CarSync(userCache.lastSyncedAt, localRepository, remoteRepository)

    @Provides @PerAndroidComponent
    fun provideSupervisorSync(
            userCache: UserCache,
            localRepository: LocalSupervisorRepository<Supervisor>,
            remoteRepository: RemoteSupervisorRepository<Supervisor>
    ) = SupervisorSync(userCache.lastSyncedAt, localRepository, remoteRepository)

    @Provides @PerAndroidComponent
    fun provideTripSync(
            userCache: UserCache,
            localRepository: LocalTripRepository<Trip>,
            remoteRepository: RemoteTripRepository<Trip>
    ) = TripSync(userCache.lastSyncedAt, localRepository, remoteRepository)

}
