package com.match.matchmate.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.match.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideJsonKotlinSerializer() : Converter.Factory {
        val json = Json { isLenient = true; ignoreUnknownKeys = true }
        return json.asConverterFactory("application/json".toMediaType())
    }
    @Provides
    @Singleton
    fun provideNormalOkHttpClient(
        @ApplicationContext appContext: Context,
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(jsonConverter: Converter.Factory, okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(jsonConverter)
            .client(okHttpClient)
            .build()
}