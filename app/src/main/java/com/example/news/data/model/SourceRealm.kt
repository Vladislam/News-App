package com.example.news.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class SourceRealm(
    @PrimaryKey
    var _id: String? = ObjectId().toHexString(),
    var id: String? = null,
    var name: String = "",
) : RealmObject()