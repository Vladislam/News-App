package com.example.news.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.model.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.ConnectionHelper
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repos: NewsRepository,
    private val connectionHelper: ConnectionHelper
) : AndroidViewModel(connectionHelper.app) {

    private var searchNewsResponse: NewsResponse? = null
    private val _searchNewsState =
        MutableStateFlow<Resource<NewsResponse>>(Resource.Success(searchNewsResponse))
    val searchNewsState get() = _searchNewsState.asStateFlow()
    var searchNewsPage = 1

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsPage = 1
        searchNewsResponse = null

        safeSearchNewsCall(searchQuery, false)
    }

    fun pagingSearchNews(searchQuery: String) = viewModelScope.launch {
        ++searchNewsPage

        safeSearchNewsCall(searchQuery, true)
    }

    private fun handleSearchNewsResponse(
        response: Response<NewsResponse>,
        isPaging: Boolean
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (isPaging) {
                    searchNewsResponse?.articles?.addAll(resultResponse.articles)
                    return Resource.Success(searchNewsResponse ?: resultResponse)
                } else if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                    return Resource.Success(resultResponse)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchNewsCall(searchQuery: String, isPaging: Boolean) {
        _searchNewsState.emit(Resource.Loading())
        try {
            if (connectionHelper.hasInternetConnection()) {
                val response = repos.searchNews(searchQuery, searchNewsPage)
                _searchNewsState.emit(handleSearchNewsResponse(response, isPaging))
            } else {
                _searchNewsState.emit(
                    Resource.Error(
                        connectionHelper.app.getString(
                            R.string.no_internet
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchNewsState.emit(
                    Resource.Error(
                        connectionHelper.app.getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> _searchNewsState.emit(
                    Resource.Error(
                        connectionHelper.app.getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }
}