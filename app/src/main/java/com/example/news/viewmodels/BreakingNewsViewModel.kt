package com.example.news.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.model.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.ConnectionHelper
import com.example.news.util.Resource
import com.example.news.util.consts.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repos: NewsRepository,
    private val connectionHelper: ConnectionHelper
) : AndroidViewModel(connectionHelper.app) {

    private var breakingNewsResponse: NewsResponse? = null
    private val _breakingNewsState =
        MutableStateFlow<Resource<NewsResponse>>(Resource.Success(breakingNewsResponse))
    val breakingNewsState get() = _breakingNewsState.asStateFlow()
    var breakingNewsPage = 1

    init {
        getBreakingNews(Constants.QUERY_LANGUAGE)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsResponse = null
        breakingNewsPage = 1

        safeBreakingNewsCall(countryCode, false)
    }

    fun pagingBreakingNews(countryCode: String) = viewModelScope.launch {
        ++breakingNewsPage

        safeBreakingNewsCall(countryCode, true)
    }

    private fun handleBreakingNewsResponse(
        response: Response<NewsResponse>,
        isPaging: Boolean
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (isPaging) {
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                } else {
                    if (breakingNewsResponse == null) {
                        breakingNewsResponse = resultResponse
                    } else {
                        breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                    }
                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall(countryCode: String, isPaging: Boolean) {
        _breakingNewsState.emit(Resource.Loading())
        try {
            if (connectionHelper.hasInternetConnection()) {
                val response = repos.getBreakingNews(countryCode, breakingNewsPage)
                _breakingNewsState.emit(handleBreakingNewsResponse(response, isPaging))
            } else {
                _breakingNewsState.emit(
                    Resource.Error(
                        connectionHelper.app.getString(
                            R.string.no_internet
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _breakingNewsState.emit(
                    Resource.Error(
                        connectionHelper.app.getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> _breakingNewsState.emit(
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