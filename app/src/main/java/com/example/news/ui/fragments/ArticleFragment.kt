package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.databinding.FragmentArticleBinding
import com.example.news.ui.activities.NewsActivity
import com.example.news.ui.fragments.base.BaseFragment
import com.example.news.util.extensions.slideUpBottomNavigationBar
import com.example.news.viewmodels.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val args: ArticleFragmentArgs by navArgs()

    private val viewModel: ArticleViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        slideUpBottomNavigationBar()

        setupViews()
        setupBackButton()
    }

    private fun setupViews() = binding.apply {
        val argArticle = args.currentArticle

        binding.isFavorite = viewModel.isArticleSaved(argArticle)

        webView.apply {
            webViewClient = WebViewClient()
            argArticle.url?.let { loadUrl(it) }
        }

        fabFav.apply {
            setOnClickListener {
                isFavorite = if (isFavorite == true) {
                    viewModel.deleteArticle(argArticle.url)
                    false
                } else {
                    viewModel.saveArticle(argArticle)
                    true
                }
            }
        }
    }

    private fun setupBackButton() =
        (requireActivity() as NewsActivity).onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
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