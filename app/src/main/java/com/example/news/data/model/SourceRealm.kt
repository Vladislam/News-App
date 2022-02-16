package com.example.news.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SourceRealm(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
) : RealmObject()