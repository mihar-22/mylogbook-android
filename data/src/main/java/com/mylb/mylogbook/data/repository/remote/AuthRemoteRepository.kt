package com.mylb.mylogbook.data.repository.remote

import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class AuthRemoteRepository @Inject constructor(
        private val endPoint: AuthEndPoint
) : AuthRepository {

    override fun register(
            name: String,
            email: String,
            password: String,
            birthdate: String
    ): Observable<Response<Unit>> = endPoint.register(name, email, password, birthdate)

    override fun login(
            email: String,
            password: String
    ): Observable<Response<Map<String, String>>> = endPoint.login(email, password)

    override fun logout(): Observable<Response<Unit>> = endPoint.logout()

    override fun check(): Observable<Response<Unit>> = endPoint.check()

    override fun forgot(email: String): Observable<Response<Unit>> = endPoint.forgot(email)
}
