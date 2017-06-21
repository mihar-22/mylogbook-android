package com.mylb.mylogbook.data.network.endpoint

import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.domain.delivery.remote.Response
import io.reactivex.Observable
import retrofit2.http.*

interface CarEndPoint {

    @GET("cars")
    fun all(): Observable<Response<List<Car>>>

    @GET("cars/{since}")
    fun all(@Path("since") updatedAfter: String): Observable<Response<List<Car>>>

    @POST("cars")
    fun create(@Body car: Car): Observable<Response<Car>>

    @PUT("cars/{car}")
    fun update(@Path("car") id: Int, @Body car: Car): Observable<Response<Unit>>

    @DELETE("cars/{car}")
    fun delete(@Path("car") id: Int): Observable<Response<Unit>>

}