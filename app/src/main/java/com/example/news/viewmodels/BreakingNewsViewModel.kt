package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.repository.NewsRepository
import com.example.news.data.model.NewsResponse
import com.example.news.util.Constants
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repos: NewsRepository,
) : ViewModel() {

    private val _breakingNewsState = MutableStateFlow<Resource<NewsResponse>>(Resource.Loading())
    val breakingNewsState get() = _breakingNewsState.asStateFlow()
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    init {
        getBreakingNews(Constants.QUERY_LANGUAGE)
    }

    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            _breakingNewsState.emit(Resource.Loading())
            val response = repos.getBreakingNews(countryCode, breakingNewsPage)
            _breakingNewsState.emit(handleBreakingNewsResponse(response))
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}