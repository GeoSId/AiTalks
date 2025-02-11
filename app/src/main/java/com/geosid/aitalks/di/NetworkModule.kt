package com.geosid.aitalks.di

import com.geosid.aitalks.constants.baseUrlOpenAI
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(
            Interceptor { chain ->
                val request: Request?
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                request = requestBuilder.build()
                chain.proceed(request)
            })
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(baseUrlOpenAI)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}