package com.example.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.ui.listeners.PagingScrollListener
import com.example.news.util.Constants.QUERY_LANGUAGE
import com.example.news.util.Constants.QUERY_PAGE_SIZE
import com.example.news.util.Resource
import com.example.news.viewmodels.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    companion object {
        private const val TAG = "BreakingNewsFragment"
    }

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BreakingNewsViewModel by viewModels()

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var pagingScrollListener: PagingScrollListener

    override fun setup(savedInstanceState: Bundle?) {
        setupRecycler()
        setupSwipeToRefresh()

        setupViewModel()
    }

    private fun setupSwipeToRefresh() {
        binding.apply {
            swipeToRefresh.setOnRefreshListener {
                viewModel.getBreakingNews(QUERY_LANGUAGE)
            }
        }
    }

    private fun setupRecycler() {
        pagingScrollListener = PagingScrollListener(viewModel::pagingBreakingNews, QUERY_LANGUAGE)

        newsAdapter = NewsAdapter { article ->
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment2(
                    article,
                    article.title ?: getString(R.string.article)
                )
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerViewBreakingNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(pagingScrollListener)
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
        pagingScrollListener.isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        pagingScrollListener.isLoading = true
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.breakingNewsState.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        if (!binding.swipeToRefresh.isRefreshing)
                            showProgressBar()
                    }
                    is Resource.Success -> {
                        if (binding.swipeToRefresh.isRefreshing) {
                            binding.swipeToRefresh.isRefreshing = false
                        } else {
                            hideProgressBar()
                        }

                        response.data?.let { newsResponse ->
                            newsAdapter.submitList(newsResponse.articles)

                            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            pagingScrollListener.isLastPage =
                                viewModel.breakingNewsPage == totalPages
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        binding.swipeToRefresh.isRefreshing = false
                        response.message?.let {
                            Log.e(TAG, "An error has occurred $it")
                        }
                    }
                }
            }
        }
    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
}