package com.example.news.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.ui.activities.base.BaseActivity
import com.example.news.viewmodels.NewsViewModel
import com.google.android.material.animation.AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
import com.google.android.material.animation.AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : BaseActivity(R.layout.activity_news) {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    val newsViewModel: NewsViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            navController =
                (supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment).navController

            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.breakingNewsFragment,
                R.id.savedNewsFragment,
                R.id.searchNewsFragment,
            ).build()

            toolbar.setupWithNavController(navController, appBarConfiguration)
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    fun slideUpBottomNavigationBar() {
        binding.bottomNavigationView.translationY = 0f
    }

    override fun setupBinding(layout: Int): ViewGroup {
        binding = ActivityNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}