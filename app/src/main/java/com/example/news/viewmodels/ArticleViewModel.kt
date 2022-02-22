package com.example.news.viewmodels

import com.example.news.repository.NewsRepository
import com.example.news.ui.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repos: NewsRepository,
) : RepositoryViewModel(repos) {

    fun isArticleSaved(article: Article): Boolean = repos.isArticleSaved(article)

}