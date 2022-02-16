package com.example.news.application

import android.app.Application
import com.example.news.util.Constants.DB_NAME
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class NewsApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}