package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.data.repository.remote.AuthRemoteRepository
import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.presentation.device.authenticator.Auth as AndroidAuthenticator
import com.mylb.mylogbook.domain.repository.AuthRepository
import com.mylb.mylogbook.presentation.di.qualifier.ForActivity
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.mylb.mylogbook.presentation.device.authenticator.Auth as Authenticator

@Module
class AuthModule {

    @Provides @PerActivity
    fun provideAuthEndPoint(retrofit: Retrofit): AuthEndPoint =
            retrofit.create(AuthEndPoint::class.java)

    @Provides @PerActivity
    fun provideAuthRepository(endPoint: AuthEndPoint, cache: UserCache): AuthRepository =
            AuthRemoteRepository(endPoint, cache)

    @Provides @PerActivity
    fun provideAuth(@ForActivity context: Context): Auth = AndroidAuthenticator(context)
}
