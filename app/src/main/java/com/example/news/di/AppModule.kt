package com.example.news.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRealmConfiguration(): RealmConfiguration = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .allowWritesOnUiThread(true)
        .allowQueriesOnUiThread(true)
        .build()

    @Provides
    fun provideRealmInstance(): Realm = Realm.getInstance(provideRealmConfiguration())
}