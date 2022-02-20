package com.example.news.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import org.bson.types.ObjectId

@RealmClass
open class ArticleRealm(
    @PrimaryKey
    var id: String? = ObjectId().toHexString(),
    var author: String? = "",
    var content: String? = "",
    var description: String = "",
    var publishedAt: String = "",
    var source: SourceRealm? = null,
    @Required
    var title: String = "",
    var url: String = "",
    var urlToImage: String = "",
) : RealmObject()