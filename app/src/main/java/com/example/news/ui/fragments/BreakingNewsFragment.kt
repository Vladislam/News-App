package com.example.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.fragments.base.BaseFragment

class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    override fun setup(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun setupBinding(view: View) {
        _binding = FragmentBreakingNewsBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}