package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import com.example.news.data.model.ArticleEntity
import com.example.news.repository.NewsRepository

open class RepositoryViewModel(
    private val repos: NewsRepository,
) : ViewModel() {
    fun saveArticle(article: ArticleEntity) {
        repos.insertArticle(article)
    }

    fun deleteArticle(url: String?) {
        repos.deleteArticle(url)
    }
}