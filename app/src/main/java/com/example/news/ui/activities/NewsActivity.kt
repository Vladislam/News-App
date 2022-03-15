package com.example.news.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.ui.activities.base.BaseActivity
import com.example.news.viewmodels.NewsViewModel
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsActivity : BaseActivity(R.layout.activity_news) {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    private val viewModel: NewsViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        setupViewModelCallbacks()
        setupBottomNavigation()
    }

    private fun setupViewModelCallbacks() {
        lifecycleScope.launch {
            viewModel.preferencesFlow.collect { preferences ->
                AppCompatDelegate.setDefaultNightMode(preferences.appTheme)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.apply {
            navController =
                (supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment).navController

            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.breakingNewsFragment,
                R.id.savedNewsFragment,
                R.id.settingsFragment,
            ).build()

            setupActionBarWithNavController(navController, appBarConfiguration)
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    fun slideUpBottomNavigationBar() {
        binding.bottomNavigationView.apply {
            ((layoutParams as CoordinatorLayout.LayoutParams).behavior as HideBottomViewOnScrollBehavior).slideUp(
                this
            )
        }
    }

    fun slideDownBottomNavigationBar() {
        binding.bottomNavigationView.apply {
            ((layoutParams as CoordinatorLayout.LayoutParams).behavior as HideBottomViewOnScrollBehavior).slideDown(
                this
            )
        }
    }

    override fun setupBinding(layout: Int): ViewGroup {
        binding = ActivityNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}