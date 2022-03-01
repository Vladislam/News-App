package com.example.news.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}