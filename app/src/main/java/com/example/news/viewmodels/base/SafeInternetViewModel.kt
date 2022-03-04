package com.example.news.viewmodels.base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel

abstract class SafeInternetViewModel(app: Application) : AndroidViewModel(app) {

    protected fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        return with(connectivityManager.getNetworkCapabilities(activeNetwork)) {
            this?.let {
                when {
                    hasTransport(TRANSPORT_WIFI) -> true
                    hasTransport(TRANSPORT_CELLULAR) -> true
                    hasTransport(TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }
}