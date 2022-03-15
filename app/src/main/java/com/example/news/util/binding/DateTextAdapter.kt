package com.example.news.util.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:dateText")
fun TextView.formattedDate(date: Date?) {
    date?.let {
        try {
            text = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(date)
        } catch (e: Exception) { }
    }
}