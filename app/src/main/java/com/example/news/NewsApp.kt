package com.example.news

import android.app.Application
import com.example.news.data.migrations.UpdateRealmMigration
import com.example.news.data.migrations.UpdateRealmMigration.Companion.SCHEMA_VERSION
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .migration(UpdateRealmMigration())
                .allowWritesOnUiThread(true)
                .build()
        )
    }
}