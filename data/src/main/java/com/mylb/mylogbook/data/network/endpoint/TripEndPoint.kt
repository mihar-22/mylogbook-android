package com.mylb.mylogbook.data.network.endpoint

import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.domain.delivery.remote.Response
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TripEndPoint {

    @GET("trips")
    fun all(): Observable<Response<List<Trip>>>

    @GET("trips/{since}")
    fun all(@Path("since") updatedAfter: String): Observable<Response<List<Trip>>>

    @POST("trips")
    fun create(@Body trip: Trip): Observable<Response<Trip>>

}
