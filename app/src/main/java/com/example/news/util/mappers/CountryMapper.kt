package com.example.news.util.mappers

import android.content.res.Resources
import com.example.news.R

class CountryMapper(resources: Resources) {

    val countryNames: Array<String> = resources.getStringArray(R.array.country_names)
    private val countryCodes: Array<String> = resources.getStringArray(R.array.country_codes)

    private val countryNamesToCodes =
        countryNames.zip(countryCodes).toMap()
    private val countryCodesToNames =
        countryCodes.zip(countryNames).toMap()

    fun mapNameToCode(name: String): String {
        return countryNamesToCodes[name] ?: ""
    }

    fun mapCodeToName(code: String): String {
        return countryCodesToNames[code] ?: ""
    }
}