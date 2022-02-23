package com.example.news.util.extensions

import com.example.news.util.RealmLiveData
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)

fun <T : RealmModel> T.copyEntity(): T = Realm.getDefaultInstance().copyFromRealm(this)