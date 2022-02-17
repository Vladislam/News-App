package com.example.news.repository

import com.example.news.api.NewsApiService
import com.example.news.data.ArticleDao
import com.example.news.ui.model.Article
import com.example.news.util.extencials.mapArticle
import com.example.news.util.extencials.mapArticleRealm
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val dao: ArticleDao,
    private val api: NewsApiService,
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun insertArticle(article: Article) {
        dao.insertArticle(article.mapArticleRealm())
    }

    suspend fun insertArticles(articles: List<Article>) {
        dao.insertArticles(articles.map { it.mapArticleRealm() })
    }

    suspend fun deleteArticle(article: Article) {
        dao.deleteArticle(article)
    }
}