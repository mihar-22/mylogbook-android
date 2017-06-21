package com.mylb.mylogbook.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mylb.mylogbook.data.database.entity.Trip
import io.reactivex.Flowable

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg resources: Trip)

    @Query("SELECT * FROM trip")
    fun all(): Flowable<List<Trip>>

    @Query("SELECT * FROM trip WHERE isAccumulated = 0")
    fun allNotAccumulated(): Flowable<List<Trip>>

    @Query("UPDATE trip SET remoteId = :remoteId WHERE id = :id")
    fun updateRemoteId(id: Int, remoteId: Int)

    @Query("SELECT id FROM car WHERE remoteId = :remoteId")
    fun findCarId(remoteId: Int): Int

    @Query("SELECT id FROM supervisor WHERE remoteId = :remoteId")
    fun findSupervisorId(remoteId: Int): Int

}
