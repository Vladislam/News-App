package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.news.R
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    override fun setup(savedInstanceState: Bundle?) {
    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}