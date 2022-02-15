package com.example.news.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.ui.activities.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : BaseActivity(R.layout.activity_news) {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var navController: NavController

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            navController = (supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment).navController
            bottomNavigationView.setupWithNavController(navController)
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