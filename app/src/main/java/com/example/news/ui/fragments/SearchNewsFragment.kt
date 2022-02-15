package com.example.news.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.news.R
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.databinding.FragmentSearchNewsBinding
import com.example.news.ui.fragments.base.BaseFragment

class SearchNewsFragment : BaseFragment(R.layout.fragment_article) {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    override fun setup(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun setupBinding(view: View) {
        _binding = FragmentSearchNewsBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}