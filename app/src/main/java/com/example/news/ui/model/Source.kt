package com.example.news.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.bson.types.ObjectId
import java.util.*

@Parcelize
data class Source(
    var _uuid: String? = UUID.randomUUID().toString(),
    var id: String? = null,
    var name: String = "",
) : Parcelable