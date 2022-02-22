package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.repository.NewsRepository
import com.example.news.ui.model.NewsResponse
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repos: NewsRepository,
) : ViewModel() {

    private val _searchNewsState = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())
    val searchNewsState get() = _searchNewsState.asStateFlow()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            _searchNewsState.emit(Resource.Loading())

            searchNewsPage = 1
            searchNewsResponse = null

            val response = repos.searchNews(searchQuery, searchNewsPage)
            _searchNewsState.emit(handleSearchNewsResponse(response))
        }
    }

    fun pagingSearchNews(searchQuery: String) = viewModelScope.launch {
        _searchNewsState.emit(Resource.Loading())

        ++searchNewsPage

        val response = repos.searchNews(searchQuery, searchNewsPage)
        _searchNewsState.emit(handlePagingSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                }

                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handlePagingSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsResponse?.articles?.addAll(resultResponse.articles)

                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}