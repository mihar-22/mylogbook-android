package com.mylb.mylogbook.data.network.endpoint

import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.repository.AuthRepository
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject

interface AuthEndPoint {

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("birthday") birthdate: String
    ): Observable<Response<Unit>>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): Observable<Response<Map<String, String>>>

    @GET("auth/logout")
    fun logout(): Observable<Response<Unit>>

    @GET("auth/check")
    fun check(): Observable<Response<Unit>>

    @FormUrlEncoded
    @POST("auth/forgot")
    fun forgot(
            @Field("email") email: String
    ): Observable<Response<Unit>>
}