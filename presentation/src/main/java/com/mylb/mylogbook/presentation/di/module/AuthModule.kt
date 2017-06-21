package com.mylb.mylogbook.presentation.di.module

import android.accounts.AccountManager
import android.content.Context
import com.mylb.mylogbook.data.network.endpoint.AuthEndPoint
import com.mylb.mylogbook.data.network.repository.RetrofitAuthRepository
import com.mylb.mylogbook.domain.auth.Auth
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteAuthRepository
import com.mylb.mylogbook.presentation.di.qualifier.ForAndroidComponent
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.mylb.mylogbook.presentation.device.authenticator.Auth as AndroidAuthenticator

@Module
class AuthModule {

    @Provides @PerAndroidComponent
    fun provideAuthEndPoint(retrofit: Retrofit): AuthEndPoint =
            retrofit.create(AuthEndPoint::class.java)

    @Provides @PerAndroidComponent
    fun provideAuthRepository(endPoint: AuthEndPoint, userCache: UserCache): RemoteAuthRepository =
            RetrofitAuthRepository(endPoint, userCache)

    @Provides @PerAndroidComponent
    fun provideAuth(accountManager: AccountManager): Auth = AndroidAuthenticator(accountManager)

    @Provides @PerAndroidComponent
    fun provideAccountManager(@ForAndroidComponent context: Context): AccountManager =
            AccountManager.get(context)

}
