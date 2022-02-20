package com.example.news.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.bson.types.ObjectId

@Parcelize
data class Article(
    var id: String? = ObjectId().toHexString(),
    var author: String? = null,
    var content: String? = "",
    var description: String = "",
    var publishedAt: String = "",
    var source: Source? = null,
    var title: String = "",
    var url: String = "",
    var urlToImage: String = "",
) : Parcelable