package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.mylb.mylogbook.data.database.entity.Car
import com.mylb.mylogbook.data.database.entity.Supervisor
import com.mylb.mylogbook.data.database.entity.Trip
import com.mylb.mylogbook.data.network.Network
import com.mylb.mylogbook.data.network.endpoint.CarEndPoint
import com.mylb.mylogbook.data.network.endpoint.SupervisorEndPoint
import com.mylb.mylogbook.data.network.endpoint.TripEndPoint
import com.mylb.mylogbook.data.network.repository.RetrofitCarRepository
import com.mylb.mylogbook.data.network.repository.RetrofitSupervisorRepository
import com.mylb.mylogbook.data.network.repository.RetrofitTripRepository
import com.mylb.mylogbook.domain.cache.UserCache
import com.mylb.mylogbook.domain.delivery.local.repository.LocalCarRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalSupervisorRepository
import com.mylb.mylogbook.domain.delivery.local.repository.LocalTripRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteCarRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteSupervisorRepository
import com.mylb.mylogbook.domain.delivery.remote.repository.RemoteTripRepository
import com.mylb.mylogbook.presentation.BuildConfig
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
import com.mylb.mylogbook.presentation.di.scope.PerAndroidComponent
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class HttpModule(private val baseUrl: String) {

    @Provides @Singleton
    fun provideOkHttpCache(@ForApplication context: Context): Cache =
            Cache(context.cacheDir, (10 * 1024 * 1024))

    @Provides @Singleton
    fun provideGson(): Gson = Network.converter

    @Provides @Singleton
    fun provideOkHttpClient(cache: Cache, userCache: UserCache): OkHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(StethoInterceptor())
            .addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                        .header("X-Requested-With", "XMLHttpRequest")
                        .method(original.method(), original.body())

                if (original.url().host() == BuildConfig.MYLB_HOST)
                    request.header("Authorization", "Bearer ${userCache.apiToken}")

                chain.proceed(request.build())
            }
            .build()

    @Provides @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

    @Provides @Singleton
    fun provideRemoteCarRepository(
            retrofit: Retrofit,
            localRepository: LocalCarRepository<Car>
    ): RemoteCarRepository<Car> {
        val endPoint = retrofit.create(CarEndPoint::class.java)

        return RetrofitCarRepository(endPoint, localRepository)
    }

    @Provides @Singleton
    fun provideRemoteSupervisorRepository(
            retrofit: Retrofit,
            localRepository: LocalSupervisorRepository<Supervisor>
    ): RemoteSupervisorRepository<Supervisor> {
        val endPoint = retrofit.create(SupervisorEndPoint::class.java)

        return RetrofitSupervisorRepository(endPoint, localRepository)
    }

    @Provides @Singleton
    fun provideRemoteTripRepository(
            retrofit: Retrofit,
            localRepository: LocalTripRepository<Trip>
    ): RemoteTripRepository<Trip>  {
        val endPoint = retrofit.create(TripEndPoint::class.java)

        return RetrofitTripRepository(endPoint, localRepository)
    }

}
