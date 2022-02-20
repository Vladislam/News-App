package com.example.news.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.Resource
import com.example.news.util.extencials.hideBottomNavigationBar
import com.example.news.util.extencials.showBottomNavigationBar
import com.example.news.util.extencials.showSnackBarWithDismiss
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

        setupViewModel()
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter { article ->
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment2(
                    article,
                    article.title,
                    viewModel.isArticleFavorite(article))
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerViewBreakingNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener (object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if(dy > 5) hideBottomNavigationBar()
                        else if (dy < -5 || (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() < 1) showBottomNavigationBar()
                    }
                })
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.breakingNewsState.collect { response ->
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

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}