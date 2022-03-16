package com.example.news.ui.fragments

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.adapters.NewsAdapter
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.ui.listeners.PagingScrollListener
import com.example.news.util.Resource
import com.example.news.util.broadcastreceiver.ConnectivityReceiver
import com.example.news.util.const.Constants.MAX_RESULTS_RESTRICTION
import com.example.news.util.const.Constants.QUERY_PAGE_SIZE
import com.example.news.util.const.Constants.SEARCH_NEWS_TIME_DELAY
import com.example.news.util.extensions.onQueryTextChanged
import com.example.news.util.extensions.slideUpBottomNavigationBar
import com.example.news.viewmodels.BreakingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment(R.layout.fragment_breaking_news) {

    companion object {
        private const val TAG = "BreakingNewsFragment"
    }

    private lateinit var searchView: SearchView

    private var isExpanded: Boolean = false

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BreakingNewsViewModel by viewModels()

    private lateinit var newsAdapter: NewsAdapter

    private lateinit var pagingScrollListener: PagingScrollListener

    @Inject
    lateinit var connectivityReceiver: ConnectivityReceiver

    override fun setup(savedInstanceState: Bundle?) {
        slideUpBottomNavigationBar()

        setupCallbacks()

        setupRecycler()
        setupSwipeToRefresh()

        setupViewModelCallbacks()
    }

    private fun setupCallbacks() {
        registerBreakingNewsCallback()
        pagingScrollListener = PagingScrollListener { viewModel.pagingBreakingNews() }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            val isExpandedScoped = WeakReference(isExpanded)
            if (isExpandedScoped.get() == true) viewModel.searchNews(searchView.query.toString())
            else viewModel.getBreakingNews()
        }
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter { article ->
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment2(
                    article,
                    article.title ?: getString(R.string.article)
                )
            findNavController().navigate(action)
        }

        binding.recyclerViewBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(pagingScrollListener)
        }
    }

    private fun setupViewModelCallbacks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorAction.collect { response ->
                response.message?.let { message ->
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_occurred_with_message, message),
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e(
                        TAG,
                        getString(R.string.error_occurred_with_message, message)
                    )
                    if (response.message == getString(R.string.no_internet) && newsAdapter.itemCount < 1) {
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.breakingNewsState.collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            if (!binding.swipeToRefresh.isRefreshing)
                                showProgressBar()
                        }
                        is Resource.Success -> {
                            hideProgressBar()

                            response.data?.let { newsResponse ->
                                showNewsList()
                                newsAdapter.submitList(newsResponse.articles)

                                val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                                pagingScrollListener.isLastPage =
                                    viewModel.breakingNewsPage == totalPages
                            } ?: showEmptyMessage()
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchNewsState.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        if (!binding.swipeToRefresh.isRefreshing)
                            showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { newsResponse ->
                            showNewsList()
                            newsAdapter.submitList(newsResponse.articles)

                            val totalPages =
                                newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            pagingScrollListener.isLastPage =
                                viewModel.searchNewsPage == totalPages || newsResponse.articles.size >= MAX_RESULTS_RESTRICTION
                        }
                    }
                    is Resource.Error -> {
                        if (newsAdapter.itemCount < 1) showEmptyMessage()
                        hideProgressBar()
                        response.message?.let { message ->
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.error_occurred_with_message, message),
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e(
                                TAG,
                                getString(R.string.error_occurred_with_message, message)
                            )
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.breaking_news_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value

        searchView.apply {
            if (pendingQuery != null && pendingQuery.isNotEmpty()) {
                searchItem.expandActionView()
                setQuery(pendingQuery, false)
            }
            onQueryTextChanged {
                viewModel.searchQuery.value = it
            }
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                registerSearchCallback()
                pagingScrollListener.registerCallback {
                    val searchView = searchView
                    viewModel.pagingSearchNews(searchView.query.toString())
                }
                isExpanded = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                registerBreakingNewsCallback()
                viewModel.backToBreakingNews()
                pagingScrollListener.registerCallback {
                    viewModel.pagingBreakingNews()
                }
                isExpanded = false
                return true
            }
        })

        var job: Job? = null
        viewModel.searchQuery.observe(viewLifecycleOwner) {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                ensureActive()
                if (it.isNotEmpty()) {
                    viewModel.searchNews(it)
                }
            }
        }
    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        registerBreakingNewsCallback()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::searchView.isInitialized) searchView.setOnQueryTextListener(null)
        connectivityReceiver.unregister(requireContext())
    }

    private fun showEmptyMessage() =
        binding.viewSwitcher.apply {
            if (nextView.id == R.id.textViewNoInternet) {
                showNext()
            }
        }

    private fun showNewsList() =
        binding.viewSwitcher.apply {
            if (nextView.id == R.id.recyclerViewBreakingNews) {
                showNext()
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

    private fun registerBreakingNewsCallback() {
        connectivityReceiver.registerCallback {
            viewModel.getBreakingNews()
            connectivityReceiver.unregister(requireContext())
        }
    }

    private fun registerSearchCallback() {
        connectivityReceiver.registerCallback {
            val et = searchView
            if (et.query.isNotEmpty()) {
                viewModel.searchNews(et.query.toString())
                connectivityReceiver.unregister(requireContext())
            }
        }
    }
}