package com.example.news.data.managers

import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.news.R
import com.example.news.data.model.Error
import com.example.news.data.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val TAG = "PreferencesManager"
        private const val COUNTRY_CODE_TAG = "country_code"
        private const val APP_THEME_TAG = "app_theme"
    }

    private val Context.dataStore by preferencesDataStore("settings")

    private object PreferencesKeys {
        val COUNTRY_CODE = stringPreferencesKey(COUNTRY_CODE_TAG)
        val APP_THEME = intPreferencesKey(APP_THEME_TAG)
    }

    private val settingsDataStore = context.dataStore

    val preferencesFlow = settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            mapUserPreferences(it)
        }

    private val _errorFlow = MutableSharedFlow<Error>()
    val errorFlow = _errorFlow.asSharedFlow()

    suspend fun updateCountry(countryCode: String) {
        if (context.resources.getStringArray(R.array.country_codes)
                .contains(countryCode.lowercase())
        ) {
            settingsDataStore.edit { preferences ->
                preferences[PreferencesKeys.COUNTRY_CODE] = countryCode
            }
        } else {
            _errorFlow.emit(Error(context.getString(R.string.error_country_code_not_exists)))
        }
    }

    suspend fun updateDarkTheme(isEnabledDark: Int) = settingsDataStore.edit { preferences ->
        preferences[PreferencesKeys.APP_THEME] = isEnabledDark
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val name = preferences[PreferencesKeys.COUNTRY_CODE]
            ?: (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso
        val appTheme =
            preferences[PreferencesKeys.APP_THEME] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        return UserPreferences(name, appTheme)
    }
}