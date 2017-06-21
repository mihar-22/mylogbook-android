package com.mylb.mylogbook.data.network.repository

import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.remote.Response
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import io.reactivex.Observable
import org.joda.time.DateTime
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class RetrofitAuthRepository @Inject constructor(
        private val endPoint: AuthEndPoint,
        private val cache: UserCache
) : RemoteAuthRepository {

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
                .doOnNext {
                    Timber.d("Successfully registered")

                    cache.email = email
                }
                .doOnError { Timber.d("Registration failed with: %s", it.message) }
    }

    override fun login(
            email: String,
            password: String
    ): Observable<Response<Map<String, String>>> {
        Timber.d("Logging in (email: %s | password: %s)", email, password)

        return endPoint.login(email, password)
                .doOnNext { response ->
                    Timber.d("Successfully logged in")

                    val data = response.data!!

                    cache.name = data["name"]!!
                    cache.email = email
                    cache.birthdate = DateTime.parse(data["birthday"]!!, Network.dateFormat)
                    cache.apiToken = data["api_token"]!!
                }
                .doOnError {
                    Timber.d("Logging in failed with: %s", it.message)
                }
    }

    override fun logout(): Observable<Response<Unit>> {
        Timber.d("Logging out")

        return endPoint.logout()
                .doOnNext {
                    Timber.d("Successfully logged out")

                    cache.destroy()
                }
                .doOnError { Timber.d("Logging out failed with: %s", it.message) }
    }

    override fun check(): Observable<Response<Unit>> {
        Timber.d("Checking if authentication is valid")

        return endPoint.check()
                .doOnNext { Timber.d("Authentication is valid") }
                .doOnError {
                    Timber.d("Checking authentication failed with: %s", it.message)

                    if (it is HttpException && it.code() == 401) cache.apiToken = null
                }
    }

    override fun forgot(email: String): Observable<Response<Unit>> {
        Timber.d("Requesting forgot password for: %s", email)

        return endPoint.forgot(email)
                .doOnNext { Timber.d("Forgot password sent") }
                .doOnError { Timber.d("Forgot password failed with: %s", it.message) }
    }

}
