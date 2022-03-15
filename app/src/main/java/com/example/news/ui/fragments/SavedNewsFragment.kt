package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.ui.adapters.NewsAdapter
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.extensions.copyEntity
import com.example.news.util.extensions.setupOnSwipeCallback
import com.example.news.util.extensions.showSnackBarWithAction
import com.example.news.util.extensions.slideUpBottomNavigationBar
import com.example.news.viewmodels.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news) {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavedViewModel by viewModels()

    private lateinit var savedNewsAdapter: NewsAdapter

    override fun setup(savedInstanceState: Bundle?) {
        slideUpBottomNavigationBar()
        setupRecycler()

        setupViewModelCallbacks()
    }

    private fun setupViewModelCallbacks() {
        viewModel.getSavedNews()
            .observe(this) { result ->
                showNextView(result.size)
                savedNewsAdapter.submitList(result.map {
                    if (it.isManaged) it.copyEntity()
                    else it
                })
            }
    }

    private fun setupRecycler() {
        savedNewsAdapter = NewsAdapter { article ->
            val action =
                SavedNewsFragmentDirections.actionSavedNewsFragment2ToArticleFragment4(
                    article,
                    article.title ?: getString(R.string.article)
                )
            findNavController().navigate(action)
        }

        binding.recyclerViewSavedNews.apply {
            setupOnSwipeCallback { viewHolder, _ ->
                val article = savedNewsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteArticle(article?.url)

                showSnackBarWithAction(
                    getString(R.string.article_has_been_deleted),
                    getString(R.string.undo)
                ) {
                    viewModel.saveArticle(article)
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstItemPosition =
                        (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    if (firstItemPosition == 0 || firstItemPosition == RecyclerView.NO_POSITION)
                        slideUpBottomNavigationBar()
                }
            })
            adapter = savedNewsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showNextView(listLength: Int) =
        binding.viewSwitcher.apply {
            if (listLength < 1) {
                if (nextView.id == R.id.textViewNoItems) {
                    showNext()
                }
            } else if (nextView.id == R.id.recyclerViewSavedNews) {
                showNext()
            }
        }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}