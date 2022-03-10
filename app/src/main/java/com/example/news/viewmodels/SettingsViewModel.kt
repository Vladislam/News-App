package com.example.news.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.managers.PreferencesDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesDataStoreManager
) : ViewModel() {

    val preferencesFlow = preferencesManager.preferencesFlow

    val errorFlow = preferencesManager.errorFlow

    fun onSaveCountryCodeClick(text: String) = viewModelScope.launch {
        preferencesManager.updateCountry(text)
    }

    fun onSwitchDarkThemeClick(checked: Boolean) = viewModelScope.launch {
        preferencesManager.updateDarkTheme(checked)
    }
}