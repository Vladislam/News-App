package com.example.news.data

import com.example.news.data.model.ArticleRealm
import com.vicpin.krealmextensions.delete
import com.vicpin.krealmextensions.queryAllAsFlowable
import com.vicpin.krealmextensions.save
import io.reactivex.Flowable
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDao @Inject constructor(
    private val realm: Realm,
) {

    suspend fun insertArticle(article: ArticleRealm) = withContext(Dispatchers.IO) {
        article.save()
    }


    fun getAllArticles(): Flowable<List<ArticleRealm>> {
        return queryAllAsFlowable()
    }

    fun articleExists(article: ArticleRealm): Boolean {
        val entries = realm.where(ArticleRealm::class.java)
            .equalTo("id", article.id)
            .or()
            .equalTo("description", article.description)
            .or()
            .equalTo("publishedAt", article.publishedAt)
            .or()
            .equalTo("title", article.title)
            .or()
            .equalTo("url", article.url)
            .findFirst()

        return entries != null
    }

    suspend fun deleteArticle(article: ArticleRealm) = withContext(Dispatchers.IO) {
        article.source?.delete {
            equalTo("id", article.source?._id)
        }
        article.delete {
            equalTo("id", article.id)
        }
    }
}