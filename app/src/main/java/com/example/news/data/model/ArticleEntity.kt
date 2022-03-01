package com.example.news.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = false)
open class ArticleEntity(
    @Json(name = "author")
    var author: String? = null,
    @Json(name = "content")
    var content: String? = null,
    @Json(name = "description")
    var description: String? = null,
    @Json(name = "publishedAt")
    var publishedAt: String? = null,
    @Json(name = "source.name")
    var source: String? = null,
    @Json(name = "title")
    var title: String? = null,
    @PrimaryKey
    @Json(name = "url")
    var url: String? = null,
    @Json(name = "urlToImage")
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
        var result = 31 * author.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + publishedAt.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + urlToImage.hashCode()
        return result
    }
}