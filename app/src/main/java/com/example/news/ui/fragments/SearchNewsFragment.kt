package com.example.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSearchNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.ui.listeners.PagingScrollListener
import com.example.news.util.Constants
import com.example.news.util.Constants.SEARCH_NEWS_TIME_DELAY
import com.example.news.util.Resource
import com.example.news.viewmodels.SearchViewModel
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

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var pagingScrollListener: PagingScrollListener

    override fun setup(savedInstanceState: Bundle?) {
        setupRecycler()

        setupViewModel()

        var job: Job? = null
        binding.editTextSearch.apply {
            tag = false
            onFocusChangeListener = OnFocusChangeListener { _, _ ->
                tag = true
            }
            addTextChangedListener { editable ->
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty() && tag as Boolean) {
                            viewModel.searchNews(editable.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycler() {
        pagingScrollListener = PagingScrollListener(
            viewModel::pagingSearchNews,
            binding.editTextSearch
        )

        searchNewsAdapter = NewsAdapter { article ->
            val action =
                SearchNewsFragmentDirections.actionSearchNewsFragment2ToArticleFragment3(
                    article,
                    article.title ?: getString(R.string.article)
                )
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerViewSearchNews.apply {
                adapter = searchNewsAdapter
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

                            val totalPages =
                                newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            pagingScrollListener.isLastPage = viewModel.searchNewsPage == totalPages
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