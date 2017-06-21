package com.mylb.mylogbook.domain.delivery.local.repository

import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.domain.resource.Resource
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.domain.resource.Trip
import io.reactivex.Flowable

interface LocalRepository<T : Resource> {

    fun insert(resources: List<T>)
    fun update(resources: List<T>)
    fun delete(resources: List<T>)
    fun all(): Flowable<List<T>>
    fun setRemoteId(id: Int, remoteId: Int)

}

interface LocalCarRepository<T : Car> : LocalRepository<T>

interface LocalSupervisorRepository<T : Supervisor> : LocalRepository<T>

interface LocalTripRepository<T : Trip> : LocalRepository<T> {

    override fun update(resources: List<T>) = Unit

    override fun delete(resources: List<T>) = Unit

    fun allNotAccumulated(): Flowable<List<T>>

}

