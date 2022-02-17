package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.repository.NewsRepository
import com.example.news.ui.model.NewsResponse
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repos: NewsRepository,
) : ViewModel() {

    private val _breakingNews = MutableSharedFlow<Resource<NewsResponse>>()
    val breakingNews get() = _breakingNews.asSharedFlow()
    private var breakingNewsPage = 1

    init {
        getBreakingNews("ru")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.emit(Resource.Loading())
        val response = repos.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.emit(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}