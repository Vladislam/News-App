package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news) {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    override fun setup(savedInstanceState: Bundle?) {

    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}