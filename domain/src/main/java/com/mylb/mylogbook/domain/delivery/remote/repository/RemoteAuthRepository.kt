package com.mylb.mylogbook.domain.delivery.remote.repository

import com.mylb.mylogbook.domain.delivery.remote.Response
import io.reactivex.Observable

interface RemoteAuthRepository {

    fun register(
            name: String, email: String, password: String, birthdate: String
    ): Observable<Response<Unit>>

    fun login(email: String, password: String): Observable<Response<Map<String, String>>>

    fun logout(): Observable<Response<Unit>>

    fun check(): Observable<Response<Unit>>

    fun forgot(email: String): Observable<Response<Unit>>

}
