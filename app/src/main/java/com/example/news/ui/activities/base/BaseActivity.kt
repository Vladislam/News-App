package com.example.news.ui.activities.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(private val layout: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setupBinding(layout))

        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)

    protected abstract fun setupBinding(layout: Int) : ViewGroup
}