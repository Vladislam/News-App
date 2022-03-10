package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import com.example.news.data.managers.PreferencesDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    preferencesManager: PreferencesDataStoreManager,
) : ViewModel() {
    val preferencesFlow = preferencesManager.preferencesFlow
}