package com.example.news.repository

import com.example.news.api.NewsApiService
import com.example.news.data.ArticleDao
import com.example.news.data.model.ArticleEntity
import com.example.news.util.RealmLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApiService,
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    fun insertArticle(article: ArticleEntity) {
        dao.insertArticle(article)
    }

    fun isArticleSaved(article: ArticleEntity): Boolean {
        return dao.articleExists(article)
    }

    fun getSavedNews(): RealmLiveData<ArticleEntity> {
        return dao.getAllArticles()
    }

    fun deleteArticle(article: ArticleEntity) {
        dao.deleteArticle(article)
    }
}