package com.example.news.viewmodels

import com.example.news.data.repository.NewsRepository
import com.example.news.viewmodels.base.BaseRepositoryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    repos: NewsRepository,
) : BaseRepositoryViewModel(repos) {

    fun getSavedNews() = repos.getSavedNews()

}