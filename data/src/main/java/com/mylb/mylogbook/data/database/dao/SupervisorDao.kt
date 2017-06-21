package com.mylb.mylogbook.data.database.dao

import android.arch.persistence.room.*
import com.mylb.mylogbook.data.database.entity.Supervisor
import io.reactivex.Flowable

@Dao
interface SupervisorDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg resources: Supervisor)

    @Update
    fun update(vararg resources: Supervisor)

    @Query("SELECT * FROM supervisor")
    fun all(): Flowable<List<Supervisor>>

    @Query("UPDATE supervisor SET remoteId = :remoteId WHERE id = :id")
    fun updateRemoteId(id: Int, remoteId: Int)

    @Query("UPDATE trip SET remoteSupervisorId = :remoteId WHERE supervisorId = :id")
    fun updateTripsRemoteSupervisorId(id: Int, remoteId: Int)

}
