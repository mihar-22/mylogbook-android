package com.mylb.mylogbook.presentation.di.module

import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.data.repository.remote.AuthRemoteRepository
import com.mylb.mylogbook.domain.repository.AuthRepository
import com.mylb.mylogbook.presentation.di.scope.PerActivity
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @Provides @PerActivity
    fun provideAuthEndPoint(retrofit: Retrofit): AuthEndPoint {
        return retrofit.create(AuthEndPoint::class.java)
    }

    @Provides @PerActivity
    fun provideAuthRepository(endPoint: AuthEndPoint): AuthRepository {
        return AuthRemoteRepository(endPoint)
    }
}
