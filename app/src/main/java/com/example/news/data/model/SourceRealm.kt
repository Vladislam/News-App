package com.example.news.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId
import java.util.*

open class SourceRealm(
    @PrimaryKey
    var _uuid: String? = UUID.randomUUID().toString(),
    var id: String? = null,
    var name: String = "",
) : RealmObject()