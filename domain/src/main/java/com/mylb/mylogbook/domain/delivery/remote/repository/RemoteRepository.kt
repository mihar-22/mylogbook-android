package com.mylb.mylogbook.domain.delivery.remote.repository

import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.resource.Car
import com.mylb.mylogbook.domain.resource.Resource
import com.mylb.mylogbook.domain.resource.Supervisor
import com.mylb.mylogbook.domain.resource.Trip
import io.reactivex.Observable
import org.joda.time.DateTime

interface RemoteRepository<T : Resource> {

    fun all(): Observable<Response<List<T>>>
    fun all(updatedAfter: DateTime): Observable<Response<List<T>>>
    fun create(resource: T): Observable<Response<T>>
    fun update(resource: T): Observable<Response<Unit>>
    fun delete(resource: T): Observable<Response<Unit>>

}

interface RemoteCarRepository<T : Car> : RemoteRepository<T>

interface RemoteSupervisorRepository<T : Supervisor> : RemoteRepository<T>

interface RemoteTripRepository<T : Trip> : RemoteRepository<T> {

    override fun update(resource: T): Observable<Response<Unit>> =
            throw UnsupportedOperationException("Cannot update a trip")

    override fun delete(resource: T): Observable<Response<Unit>> =
            throw UnsupportedOperationException("Cannot delete trip")

}
