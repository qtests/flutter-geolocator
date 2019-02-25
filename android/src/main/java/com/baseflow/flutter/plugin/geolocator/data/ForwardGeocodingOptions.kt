package com.baseflow.flutter.plugin.geolocator.data

import com.baseflow.flutter.plugin.geolocator.utils.LocaleConverter
import java.util.*

class ForwardGeocodingOptions(val addressToLookup: String, locale: Locale) : GeocodingOptions(locale) {
    companion object {

        fun parseArguments(arguments: Any): ForwardGeocodingOptions {
            val argumentMap = arguments as Map<String, String>

            val addressToLookup = argumentMap["address"]
            var locale: Locale? = null

            if (argumentMap.containsKey("localeIdentifier")) {
                locale = LocaleConverter.fromLanguageTag(argumentMap["localeIdentifier"]!!)
            }

            return ForwardGeocodingOptions(addressToLookup!!, locale!!)
        }
    }
}
