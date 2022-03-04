package com.example.news.viewmodels

import com.example.news.data.model.ArticleEntity
import com.example.news.repository.NewsRepository
import com.example.news.viewmodels.base.RepositoryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repos: NewsRepository,
) : RepositoryViewModel(repos) {

    fun isArticleSaved(article: ArticleEntity): Boolean = repos.isArticleSaved(article)

}