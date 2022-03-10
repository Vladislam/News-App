package com.example.news.ui.fragments

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.ui.listeners.PagingScrollListener
import com.example.news.util.Resource
import com.example.news.util.broadcastreceiver.ConnectivityReceiver
import com.example.news.util.const.Constants.QUERY_PAGE_SIZE
import com.example.news.viewmodels.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    @Inject
    lateinit var connectivityReceiver: ConnectivityReceiver

    override fun setup(savedInstanceState: Bundle?) {
        setupCallbacks()

        setupRecycler()
        setupSwipeToRefresh()

        setupViewModel()
    }

    private fun setupCallbacks() {
        connectivityReceiver.registerCallback {
            viewModel.getBreakingNews()
            connectivityReceiver.unregister(requireContext())
        }
    }

    private fun setupSwipeToRefresh() {
        binding.apply {
            swipeToRefresh.setOnRefreshListener {
                viewModel.getBreakingNews()
            }
        }
    }

    private fun setupRecycler() {
        pagingScrollListener = PagingScrollListener { viewModel.pagingBreakingNews() }

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
        binding.swipeToRefresh.isRefreshing = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        pagingScrollListener.isLoading = true
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.breakingNewsState.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        if (!binding.swipeToRefresh.isRefreshing)
                            showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()

                        response.data?.let { newsResponse ->
                            newsAdapter.submitList(newsResponse.articles)

                            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            pagingScrollListener.isLastPage =
                                viewModel.breakingNewsPage == totalPages
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            Toast.makeText(
                                requireContext(),
                                String.format(
                                    getString(R.string.error_occurred_with_message),
                                    message
                                ),
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e(TAG, "An error has occurred $message")
                        }
                        if (response.message == getString(R.string.no_internet)) {
                            connectivityReceiver.register(
                                requireContext(),
                                IntentFilter().apply {
                                    addAction("android.net.conn.CONNECTIVITY_CHANGE")
                                }
                            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        connectivityReceiver.unregister(requireContext())
    }
}