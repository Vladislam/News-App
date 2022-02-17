package com.example.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.Resource
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    companion object {
        private const val TAG = "BreakingNewsFragment"
    }

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    private lateinit var newsAdapter: NewsAdapter

    override fun setup(savedInstanceState: Bundle?) {
        setupRecycler()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.breakingNews.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { newsResponse ->
                            newsAdapter.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let {
                            Log.e(TAG, "An error has occurred $it")
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter()

        binding.apply {
            recyclerViewBreakingNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
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