package com.mylb.mylogbook.data.repository.remote

import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.web.Response
import com.mylb.mylogbook.domain.repository.AuthRepository
import io.reactivex.Observable
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthRemoteRepository @Inject constructor(
        private val endPoint: AuthEndPoint,
        private val cache: UserCache
) : AuthRepository {

    override fun register(
            name: String,
            email: String,
            password: String,
            birthdate: String
    ): Observable<Response<Unit>> {
        Timber.d(
                "Registering account (name: %s | email: %s | password: %s | birthdate: %s)",
                name, email, password, birthdate
        )

        return endPoint.register(name, email, password, birthdate)
                .doOnNext { cache.email = email }
    }

    override fun login(
            email: String,
            password: String
    ): Observable<Response<Map<String, String>>> {
        Timber.d("Logging in (email: %s | password: %s)", email, password)

        return endPoint.login(email, password)
                .doOnNext { response ->
                    val data = response.data!!

                    cache.name = data["name"]!!
                    cache.email = email
                    cache.birthdate = data["birthday"]!!
                    cache.apiToken = data["api_token"]!!
                }
    }

    override fun logout(): Observable<Response<Unit>> {
        Timber.d("Logging out")

        return endPoint.logout().doOnNext { cache.destroy() }
    }

    override fun check(): Observable<Response<Unit>> {
        Timber.d("Checking if authentication is valid")

        return endPoint.check().doOnError {
            Timber.d("Checking authentication failed with: %s", it.message)

            if (it is HttpException && it.code() == 401) cache.apiToken = null
        }
    }

    override fun forgot(email: String): Observable<Response<Unit>> {
        Timber.d("Requesting forgot password for: %s", email)

        return endPoint.forgot(email)
    }
}
