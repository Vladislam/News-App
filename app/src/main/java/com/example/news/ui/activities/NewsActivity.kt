package com.example.news.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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
        setupViewModel()
        setupBottomNavigation()
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            viewModel.preferencesFlow.collect { preferences ->
                AppCompatDelegate.setDefaultNightMode(
                    if (preferences.darkTheme) {
                        MODE_NIGHT_YES
                    } else {
                        MODE_NIGHT_NO
                    }
                )
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
                R.id.searchNewsFragment,
                R.id.settingsFragment,
            ).build()

            toolbar.setupWithNavController(navController, appBarConfiguration)
            bottomNavigationView.apply {
                setOnItemReselectedListener { menuItem ->
                    val navOptions = NavOptions.Builder().setPopUpTo(menuItem.itemId, true).build()
                    navController.navigate(menuItem.itemId, null, navOptions)
                }

                bottomNavigationView.setupWithNavController(navController)
            }
        }
    }

    fun slideUpBottomNavigationBar() {
        binding.bottomNavigationView.apply {
            ((layoutParams as CoordinatorLayout.LayoutParams).behavior as HideBottomViewOnScrollBehavior).slideUp(
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