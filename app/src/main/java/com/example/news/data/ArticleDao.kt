package com.example.news.data

import com.example.news.data.model.ArticleEntity
import com.example.news.util.RealmLiveData
import com.example.news.util.extensions.asLiveData
import io.realm.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDao @Inject constructor(
    private val realm: Realm,
) {

    fun insertArticle(article: ArticleEntity) {
        realm.executeTransactionAsync {
            it.insertOrUpdate(article)
        }
    }

    fun getAllArticles(): RealmLiveData<ArticleEntity> =
        realm.where(ArticleEntity::class.java).findAllAsync().asLiveData()

    fun articleExists(article: ArticleEntity): Boolean {
        val entries = realm.where(ArticleEntity::class.java)
            .equalTo(article::description.name, article.description)
            .or()
            .equalTo(article::publishedAt.name, article.publishedAt)
            .or()
            .equalTo(article::title.name, article.title)
            .or()
            .equalTo(article::url.name, article.url)
            .findFirst()

        return entries != null
    }

    fun deleteArticle(url: String?) {
        realm.executeTransactionAsync {
            val articleToDelete = it.where(ArticleEntity::class.java)
                .equalTo(ArticleEntity::url.name, url)
                .findFirst()

            articleToDelete?.deleteFromRealm()
        }
    }
}