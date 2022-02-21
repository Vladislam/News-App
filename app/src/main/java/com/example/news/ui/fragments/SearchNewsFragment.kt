package com.example.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSearchNewsBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.Constants
import com.example.news.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.example.news.util.Resource
import com.example.news.util.extencials.hideBottomNavigationBar
import com.example.news.util.extencials.showBottomNavigationBar
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchNewsFragment : BaseFragment(R.layout.fragment_search_news) {

    companion object {
        private const val TAG = "BreakingNewsFragment"
    }

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchNewsAdapter: NewsAdapter

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    override fun setup(savedInstanceState: Bundle?) {
        setupRecycler()

        setupViewModel()

        var job: Job? = null
        binding.editTextSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun setupRecycler() {
        searchNewsAdapter = NewsAdapter { article ->
            val action =
                SearchNewsFragmentDirections.actionSearchNewsFragment2ToArticleFragment3(
                    article,
                    article.title)
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerViewSearchNews.apply {
                adapter = searchNewsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener (this@SearchNewsFragment.scrollListener)
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (dy >= 5) {
                hideBottomNavigationBar()
            } else if (dy < -5 || firstVisibleItemPosition < 1) {
                showBottomNavigationBar()
            }

            if (shouldPaginate){
                viewModel.pagingSearchNews(binding.editTextSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchNewsState.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { newsResponse ->
                            searchNewsAdapter.submitList(newsResponse.articles)

                            val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.searchNewsPage == totalPages
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

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}