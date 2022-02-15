package com.example.news.ui.fragments.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment(layout: Int) : Fragment(layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)

        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)

    protected abstract fun setupBinding(view: View)
}