package com.example.news.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.ui.activities.base.BaseActivity
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : BaseActivity(R.layout.activity_news) {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

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