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
import com.example.news.util.extentions.showSnackBarWithDismiss
import com.example.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleFragmentArgs by navArgs()

    override val viewModel: NewsViewModel
        get() = (activity as NewsActivity).newsViewModel

    override fun setup(savedInstanceState: Bundle?) {
        (requireActivity() as NewsActivity).slideUpBottomNavigationBar()

        val argArticle = args.currentArticle

        (requireActivity() as NewsActivity).onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        binding.apply {
            isFavorite = viewModel.isArticleSaved(argArticle)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}