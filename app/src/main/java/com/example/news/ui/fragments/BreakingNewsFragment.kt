package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    override fun setup(savedInstanceState: Bundle?) {

    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}