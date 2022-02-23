package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.model.ArticleEntity
import com.example.news.repository.NewsRepository
import kotlinx.coroutines.launch

open class RepositoryViewModel(
    private val repos: NewsRepository,
) : ViewModel() {
    fun saveArticle(article: ArticleEntity) {
        repos.insertArticle(article)
    }

    fun deleteArticle(article: ArticleEntity) {
        repos.deleteArticle(article)
    }
}