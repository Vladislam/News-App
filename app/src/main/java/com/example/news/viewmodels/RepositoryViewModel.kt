package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import com.example.news.repository.NewsRepository
import com.example.news.ui.model.Article

open class RepositoryViewModel(
    private val repos: NewsRepository,
) : ViewModel() {
    fun saveArticle(article: Article) = repos.insertArticle(article)

    fun deleteArticle(article: Article) = repos.deleteArticle(article)
}