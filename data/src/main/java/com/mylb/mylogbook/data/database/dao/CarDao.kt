package com.mylb.mylogbook.data.database.dao

import android.arch.persistence.room.*
import com.mylb.mylogbook.data.database.entity.Car
import io.reactivex.Flowable

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg resources: Car)

    @Update
    fun update(vararg resources: Car)

    @Query("SELECT * FROM car")
    fun all(): Flowable<List<Car>>

    @Query("UPDATE car SET remoteId = :remoteId WHERE id = :id")
    fun updateRemoteId(id: Int, remoteId: Int)

    @Query("UPDATE trip SET remoteCarId = :remoteId WHERE carId = :id")
    fun updateTripsRemoteCarId(id: Int, remoteId: Int)

}