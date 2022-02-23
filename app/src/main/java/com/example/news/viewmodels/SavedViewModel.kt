package com.example.news.viewmodels

import com.example.news.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repos: NewsRepository,
) : RepositoryViewModel(repos) {

    fun getSavedNews() = repos.getSavedNews()

}