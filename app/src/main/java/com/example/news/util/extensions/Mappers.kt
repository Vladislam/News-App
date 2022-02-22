package com.example.news.util.extensions

import com.example.news.data.model.ArticleRealm
import com.example.news.data.model.SourceRealm
import com.example.news.ui.model.Article
import com.example.news.ui.model.Source

fun ArticleRealm.mapArticle(): Article =
    Article(uuid = uuid,
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source?.mapSource(),
        title = title,
        url = url,
        urlToImage = urlToImage)

fun SourceRealm.mapSource(): Source =
    Source(id = id, name = name)

fun Article.mapArticleRealm(): ArticleRealm =
    ArticleRealm(uuid = uuid,
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source?.mapSourceRealm(),
        title = title,
        url = url,
        urlToImage = urlToImage)

fun Source.mapSourceRealm(): SourceRealm =
    SourceRealm(id = id, name = name)