package com.example.news.repository

import com.example.news.data.ArticleDao
import com.example.news.ui.model.Article
import com.example.news.util.extencials.mapArticle
import com.example.news.util.extencials.mapArticleRealm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(private val dao: ArticleDao) {

    suspend fun getAllArticles(): List<Article> = dao.getAllArticles().map { it.mapArticle() }

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