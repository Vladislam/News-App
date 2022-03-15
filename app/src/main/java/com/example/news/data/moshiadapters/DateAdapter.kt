package com.example.news.data.moshiadapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter {

    @FromJson
    @DateString
    fun fromJson(date: String?): Date {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(date ?: "")
            ?: throw IllegalArgumentException("Wrong date pattern")
    }

    @ToJson
    fun toJson(@DateString date: Date?): String {
        return date.toString()
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class DateString