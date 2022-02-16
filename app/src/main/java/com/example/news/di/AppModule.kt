package com.example.news.di

import com.example.news.data.migrations.UpdateRealmMigration
import com.example.news.util.Constants.DB_SCHEME_VERSION
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
    fun provideRealmConfig(): RealmConfiguration = RealmConfiguration.Builder()
        .schemaVersion(DB_SCHEME_VERSION)
        .migration(UpdateRealmMigration())
        .build()

    @Provides
    fun provideRealmInstance(
        configuration: RealmConfiguration,
    ): Realm = Realm.getInstance(configuration)
}