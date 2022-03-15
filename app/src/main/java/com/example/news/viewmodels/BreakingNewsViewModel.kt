package com.example.news.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.data.managers.PreferencesDataStoreManager
import com.example.news.data.model.NewsResponse
import com.example.news.data.repository.NewsRepository
import com.example.news.util.ConnectionHelper
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repos: NewsRepository,
    private val connectionHelper: ConnectionHelper,
    state: SavedStateHandle,
    preferencesManager: PreferencesDataStoreManager,
) : AndroidViewModel(connectionHelper.app) {

    companion object {
        private const val KEY_SEARCH_QUERY = "search_query"
    }

    val searchQuery = state.getLiveData(KEY_SEARCH_QUERY, "")

    private val _errorChannel = Channel<Resource.Error<NewsResponse>>()
    val errorAction = _errorChannel.receiveAsFlow()

    private var breakingNewsResponse: NewsResponse? = null
    private val _breakingNewsState =
        MutableStateFlow<Resource<NewsResponse>>(Resource.Success(breakingNewsResponse))
    val breakingNewsState = _breakingNewsState.asStateFlow()
    var breakingNewsPage = 1

    private var searchNewsResponse: NewsResponse? = null
    private val _searchNewsState =
        MutableSharedFlow<Resource<NewsResponse>>()
    val searchNewsState get() = _searchNewsState.asSharedFlow()
    var searchNewsPage = 1

    private val preferencesFlow = preferencesManager.preferencesFlow

    init {
        viewModelScope.launch {
            preferencesFlow.collect {
                getBreakingNews()
            }
        }
    }

    fun backToBreakingNews() = viewModelScope.launch {
        if (breakingNewsResponse != null)
            _breakingNewsState.emit(Resource.Success(breakingNewsResponse))
        else getBreakingNews()
    }

    fun getBreakingNews() =
        viewModelScope.launch {
            safeBreakingNewsCall(preferencesFlow.first().countryCode, false)
        }

    fun pagingBreakingNews() = viewModelScope.launch {
        safeBreakingNewsCall(preferencesFlow.first().countryCode, true)
    }

    private fun handleBreakingNewsResponse(
        response: Response<NewsResponse>,
        isPaging: Boolean
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (isPaging || breakingNewsResponse != null) {
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                } else {
                    breakingNewsResponse = resultResponse
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
                if (isPaging) {
                    ++breakingNewsPage
                } else {
                    breakingNewsResponse = null
                    breakingNewsPage = 1
                }
                val response = repos.getBreakingNews(countryCode, breakingNewsPage)
                val result = handleBreakingNewsResponse(response, isPaging)
                if (result is Resource.Error<*>) issueError(
                    result.message ?: connectionHelper.app.getString(R.string.unexpected_error)
                )
                else if (result is Resource.Success<*>) _breakingNewsState.emit(result)
            } else {
                issueError(connectionHelper.app.getString(R.string.no_internet))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> issueError(connectionHelper.app.getString(R.string.network_failure))
                else -> issueError(connectionHelper.app.getString(R.string.conversion_error))
            }
        }
    }

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
                _searchNewsState.emit(Resource.Error(connectionHelper.app.getString(R.string.no_internet)))
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
                else -> _searchNewsState.emit(Resource.Error(connectionHelper.app.getString(R.string.conversion_error)))
            }
        }
    }

    private suspend fun issueError(msg: String) {
        _breakingNewsState.emit(Resource.Success(breakingNewsResponse))
        _errorChannel.send(Resource.Error(msg))
    }
}