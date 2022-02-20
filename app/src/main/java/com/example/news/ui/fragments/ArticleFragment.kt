package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.extencials.showBottomNavigationBar
import com.example.news.util.extencials.showSnackBarWithDismiss
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    companion object {
        private const val IS_FAVORITE_STATE_KEY = "isFavorite"
    }

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleFragmentArgs by navArgs()

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    override fun setup(savedInstanceState: Bundle?) {
        showBottomNavigationBar()
        val argArticle = args.currentArticle

        (requireActivity() as NewsActivity).onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        binding.apply {
            isFavorite = args.isFavorite

            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(argArticle.url)
            }

            fabAddToFav.apply {
                setOnClickListener {
                    viewModel.saveArticle(argArticle)

                    isFavorite = true

                    showSnackBarWithDismiss(R.string.article_has_been_saved)
                }
            }
            fabDeleteFromFav.apply {
                setOnClickListener {
                    viewModel.deleteArticle(argArticle)

                    isFavorite = false

                    showSnackBarWithDismiss(R.string.article_has_been_deleted)
                }
            }
        }
    }

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            binding.isFavorite = savedInstanceState.getBoolean(IS_FAVORITE_STATE_KEY)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_FAVORITE_STATE_KEY, binding.isFavorite ?: args.isFavorite)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}