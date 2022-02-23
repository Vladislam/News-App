package com.example.news.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class ArticleEntity(
    var author: String? = null,
    var content: String? = null,
    var description: String = "",
    var publishedAt: String = "",
    var source: SourceEntity? = null,
    var title: String = "",
    @PrimaryKey
    var url: String = "",
    var urlToImage: String? = null,
) : RealmObject(), Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArticleEntity) return false

        if (author != other.author) return false
        if (content != other.content) return false
        if (description != other.description) return false
        if (publishedAt != other.publishedAt) return false
        if (source != other.source) return false
        if (title != other.title) return false
        if (url != other.url) return false
        if (urlToImage != other.urlToImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 31 * (author?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + description.hashCode()
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + urlToImage.hashCode()
        return result
    }

    fun copy(): ArticleEntity =
        ArticleEntity(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source?.copy(),
            title = title,
            url = url,
            urlToImage = urlToImage,
        )
}