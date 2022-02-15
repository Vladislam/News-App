package com.example.news.ui.fragments

import android.os.Bundle
import android.view.View
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.ui.fragments.base.BaseFragment

class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news) {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    override fun setup(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun setupBinding(view: View) {
        _binding = FragmentSavedNewsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}