package com.example.news.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    @Json(name = "articles")
    val articles: MutableList<ArticleEntity>,
    @Json(name = "status")
    val status: String,
    @Json(name = "totalResults")
    val totalResults: Int,
)