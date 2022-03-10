package com.example.news.data.repository

import com.example.news.api.NewsApiService
import com.example.news.data.ArticleDao
import com.example.news.data.model.ArticleEntity
import com.example.news.data.model.NewsResponse
import com.example.news.util.RealmLiveData
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApiService,
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> =
        api.searchForNews(searchQuery, pageNumber)

    fun insertArticle(article: ArticleEntity) =
        dao.insertArticle(article)

    fun isArticleSaved(article: ArticleEntity): Boolean =
        dao.articleExists(article)

    fun getSavedNews(): RealmLiveData<ArticleEntity> =
        dao.getAllArticles()

    fun deleteArticle(url: String?) =
        dao.deleteArticle(url)
}