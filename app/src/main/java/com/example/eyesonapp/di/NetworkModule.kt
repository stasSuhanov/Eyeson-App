package com.example.eyesonapp.di

import com.example.eyesonapp.BuildConfig
import com.example.eyesonapp.data.api.EyesonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.run {
                proceed(request()
                    .newBuilder()
                    .addHeader(AUTHORIZATION_HEADER, BuildConfig.EYESON_API_KEY)
                    .build()
                )
            }
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.EYESON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideEyesonApi(retrofit: Retrofit): EyesonApi {
        return retrofit.create(EyesonApi::class.java)
    }
}

const val AUTHORIZATION_HEADER = "Authorization"