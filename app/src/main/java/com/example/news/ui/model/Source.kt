package com.example.news.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.bson.types.ObjectId

@Parcelize
data class Source(
    var _id: String? = ObjectId().toHexString(),
    var id: String? = null,
    var name: String = "",
) : Parcelable