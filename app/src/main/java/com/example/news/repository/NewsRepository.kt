package com.example.news.repository

import com.example.news.api.NewsApiService
import com.example.news.data.ArticleDao
import com.example.news.data.model.ArticleRealm
import com.example.news.ui.model.Article
import com.example.news.util.RealmLiveData
import com.example.news.util.extensions.mapArticleRealm
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

    fun insertArticle(article: Article) {
        dao.insertArticle(article.mapArticleRealm())
    }

    fun isArticleSaved(article: Article): Boolean {
        return dao.articleExists(article.mapArticleRealm())
    }

    fun getSavedNews(): RealmLiveData<ArticleRealm> {
        return dao.getAllArticles()
    }

    fun deleteArticle(article: Article) {
        dao.deleteArticle(article.mapArticleRealm())
    }
}