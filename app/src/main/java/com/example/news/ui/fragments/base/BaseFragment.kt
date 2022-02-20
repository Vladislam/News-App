package com.example.news.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.viewmodels.NewsViewModel

abstract class BaseFragment(layout: Int) : Fragment(layout) {

    abstract val viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)

    protected abstract fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View
}