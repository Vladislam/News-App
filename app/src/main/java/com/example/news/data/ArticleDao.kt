package com.example.news.data

import com.example.news.data.model.ArticleRealm
import com.example.news.ui.model.Article
import io.realm.Realm
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ArticleDao @Inject constructor(
    private val realm: Realm,
) {

    suspend fun insertArticle(article: ArticleRealm) {
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.insert(article)
        }
    }

    suspend fun insertArticles(articles: List<ArticleRealm>){
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.copyToRealm(articles)
        }
    }

    suspend fun getAllArticles(): List<ArticleRealm> {
        val articles = mutableListOf<ArticleRealm>()

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            articles.addAll(transaction
                .where(ArticleRealm::class.java)
                .findAll()
            )
        }
        return articles
    }

    suspend fun deleteArticle(article: Article) {
        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            val dbArticle = transaction.where(ArticleRealm::class.java)
                .equalTo("id", article.id)
                .findFirst()

            dbArticle?.source?.deleteFromRealm()
            dbArticle?.deleteFromRealm()
        }
    }
}