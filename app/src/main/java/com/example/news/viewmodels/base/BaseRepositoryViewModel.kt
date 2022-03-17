package com.example.news.viewmodels.base

import androidx.lifecycle.ViewModel
import com.example.news.data.model.ArticleEntity
import com.example.news.data.repository.NewsRepository

abstract class BaseRepositoryViewModel(
    protected val repos: NewsRepository,
) : ViewModel() {
    fun saveArticle(article: ArticleEntity) {
        repos.insertArticle(article)
    }

    fun deleteArticle(url: String?) {
        repos.deleteArticle(url)
    }
}