package com.example.news.util.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.news.R

@BindingAdapter("app:imageUrl", "app:placeholder", requireAll = false)
fun ImageView.loadImage(url: String?, placeholder: Drawable? = null) {
    Glide.with(this)
        .load(url)
        .placeholder(
            placeholder ?: ContextCompat.getDrawable(
                this.context,
                R.drawable.no_image_placeholder
            )
        )
        .error(R.drawable.no_image_placeholder)
        .into(this)
}