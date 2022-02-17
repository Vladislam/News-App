package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.databinding.FragmentSearchNewsBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : BaseFragment(R.layout.fragment_article) {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    override fun setup(savedInstanceState: Bundle?) {

    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}