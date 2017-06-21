package com.mylb.mylogbook.data.network.endpoint

import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.domain.delivery.remote.Response
import io.reactivex.Observable
import retrofit2.http.*

interface  SupervisorEndPoint {

    @GET("supervisors")
    fun all(): Observable<Response<List<Supervisor>>>

    @GET("supervisors/{since}")
    fun all(@Path("since") updatedAfter: String): Observable<Response<List<Supervisor>>>

    @POST("supervisors")
    fun create(@Body supervisor: Supervisor): Observable<Response<Supervisor>>

    @PUT("supervisors/{supervisor}")
    fun update(@Path("supervisor") id: Int, @Body supervisor: Supervisor): Observable<Response<Unit>>

    @DELETE("supervisors/{supervisor}")
    fun delete(@Path("supervisor") id: Int): Observable<Response<Unit>>

}
