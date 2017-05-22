package com.mylb.mylogbook.presentation.di.module

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mylb.mylogbook.presentation.di.qualifier.ForApplication
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
    fun provideOkHttpCache(@ForApplication context: Context): Cache {
        return Cache(context.cacheDir, (10 * 1024 * 1024))
    }

    @Provides @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient  {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor({ chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                            .header("X-Requested-With", "XMLHttpRequest")
                            .method(original.method(), original.body())
                            .build()

                    chain.proceed(request)
                })
                .build()
    }

    @Provides @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build()
    }
}
