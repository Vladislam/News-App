package com.example.news.di

import com.example.news.api.NewsApiService
import com.example.news.data.moshiadapters.DateAdapter
import com.example.news.data.moshiadapters.SourceNameAdapter
import com.example.news.util.const.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(SourceNameAdapter())
        .add(DateAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)
}