package com.mylb.mylogbook.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.mylb.mylogbook.data.database.converter.EncounterConverters
import com.mylb.mylogbook.data.database.converter.JodaConverters
import com.mylb.mylogbook.data.database.dao.CarDao
import com.mylb.mylogbook.data.database.dao.SupervisorDao
import com.mylb.mylogbook.data.database.dao.TripDao
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip

@Database(entities = arrayOf(Car::class, Supervisor::class, Trip::class), version = 1, exportSchema = false)
@TypeConverters(JodaConverters::class, EncounterConverters::class)
abstract class Database : RoomDatabase() {

    abstract fun cars(): CarDao
    abstract fun supervisors(): SupervisorDao
    abstract fun trips(): TripDao

}
