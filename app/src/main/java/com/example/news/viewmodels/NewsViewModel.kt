package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.repository.NewsRepository
import com.example.news.ui.model.Article
import com.example.news.ui.model.NewsResponse
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repos: NewsRepository,
) : ViewModel() {

    private val _breakingNewsState = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())
    val breakingNewsState get() = _breakingNewsState.asStateFlow()
    private var breakingNewsPage = 1

    private val _searchNewsState = MutableSharedFlow<Resource<NewsResponse>>()
    val searchNewsState get() = _searchNewsState.asSharedFlow()
    private var searchNewsPage = 1

    init {
        getBreakingNews("ru")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNewsState.emit(Resource.Loading())
        val response = repos.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNewsState.emit(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNewsState.emit(Resource.Loading())
        val response = repos.searchNews(searchQuery, searchNewsPage)
        _searchNewsState.emit(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repos.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repos.deleteArticle(article)
    }

    fun getSavedNews() = repos.getSavedNews()

    fun isArticleFavorite(article: Article) = repos.isArticleSaved(article)
}