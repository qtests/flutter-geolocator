package com.baseflow.flutter.plugin.geolocator.utils

import java.util.*

object LocaleConverter {
    private val LOCALE_DELIMITER = "_"

    fun fromLanguageTag(languageTag: String): Locale? {
        var language: String? = null
        var country: String? = null
        var variant: String? = null
        val tokenizer = StringTokenizer(languageTag, LOCALE_DELIMITER, false)

        if (tokenizer.hasMoreTokens()) {
            language = tokenizer.nextToken()
        }

        if (tokenizer.hasMoreTokens()) {
            country = tokenizer.nextToken()
        }

        if (tokenizer.hasMoreTokens()) {
            variant = tokenizer.nextToken()
        }

        if (language != null && country != null && variant != null) {
            return Locale(language, country, variant)
        } else if (language != null && country != null) {
            return Locale(language, country)
        } else if (language != null) {
            return Locale(language)
        }

        return null
    }
}
