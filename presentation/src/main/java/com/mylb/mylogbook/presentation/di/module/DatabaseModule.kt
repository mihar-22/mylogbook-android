package com.mylb.mylogbook.presentation.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.mylb.mylogbook.data.database.Database
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.database.repository.RoomCarRepository
import com.mylb.mylogbook.data.database.repository.RoomSupervisorRepository
import com.mylb.mylogbook.data.database.repository.RoomTripRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ForApplication context: Context): Database =
            Room.databaseBuilder(context, Database::class.java, "app-database").build()

    @Provides @Singleton
    fun provideLocalCarRepository(database: Database): LocalCarRepository<Car> =
            RoomCarRepository(database.cars())

    @Provides @Singleton
    fun provideLocalSupervisorRepository(database: Database): LocalSupervisorRepository<Supervisor> =
            RoomSupervisorRepository(database.supervisors())

    @Provides @Singleton
    fun provideLocalTripRepository(database: Database): LocalTripRepository<Trip> =
            RoomTripRepository(database.trips())

}
