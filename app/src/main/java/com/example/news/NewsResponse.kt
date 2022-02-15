package com.example.news

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)