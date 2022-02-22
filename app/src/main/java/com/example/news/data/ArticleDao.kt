package com.example.news.data

import com.example.news.data.model.ArticleRealm
import com.example.news.data.model.SourceRealm
import com.example.news.util.RealmLiveData
import com.example.news.util.extensions.asLiveData
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDao @Inject constructor(
    private val realm: Realm,
) {

    fun insertArticle(article: ArticleRealm) {
        realm.executeTransactionAsync {
            it.insert(article)
        }
    }

    fun getAllArticles(): RealmLiveData<ArticleRealm> =
        realm.where(ArticleRealm::class.java).findAllAsync().asLiveData()


    fun articleExists(article: ArticleRealm): Boolean {
        val entries = realm.where(ArticleRealm::class.java)
            .equalTo("uuid", article.uuid)
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

    fun deleteArticle(article: ArticleRealm) {
        realm.executeTransactionAsync {
            it.where(SourceRealm::class.java)
                .equalTo("_uuid", article.source?._uuid)
                .findFirst()
                ?.deleteFromRealm()
            it.where(ArticleRealm::class.java)
                .equalTo("uuid", article.uuid)
                .findFirst()
                ?.deleteFromRealm()
        }
    }
}