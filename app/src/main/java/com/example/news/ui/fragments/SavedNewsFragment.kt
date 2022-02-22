package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.extensions.mapArticle
import com.example.news.viewmodels.SavedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : BaseFragment(R.layout.fragment_saved_news) {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavedViewModel by viewModels()

    private lateinit var savedNewsAdapter: NewsAdapter

    override fun setup(savedInstanceState: Bundle?) {
        setupRecycler()

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.getSavedNews()
            .observe(this) { result ->
                savedNewsAdapter.submitList(result.map { it.mapArticle() })
            }
    }

    private fun setupRecycler() {
        savedNewsAdapter = NewsAdapter { article ->
            val action =
                SavedNewsFragmentDirections.actionSavedNewsFragment2ToArticleFragment4(
                    article,
                    article.title
                )
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerViewSavedNews.apply {
                ItemTouchHelper(object :
                    ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder,
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val article = savedNewsAdapter.currentList[viewHolder.adapterPosition]
                        viewModel.deleteArticle(article)
                        Snackbar.make(
                            requireView(),
                            getString(R.string.article_has_been_deleted),
                            Snackbar.LENGTH_LONG
                        ).apply {
                            setAction(getString(R.string.undo)) {
                                viewModel.saveArticle(article)
                            }
                            show()
                        }
                    }
                }).attachToRecyclerView(this)

                adapter = savedNewsAdapter
                layoutManager = LinearLayoutManager(activity)
            }
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