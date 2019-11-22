package com.baseflow.flutter.plugin.geolocator.data

import com.baseflow.flutter.plugin.geolocator.utils.LocaleConverter
import java.util.*

class ReverseGeocodingOptions(val coordinate: Coordinate, locale: Locale?) : GeocodingOptions(locale) {
    companion object {

        fun parseArguments(arguments: Any): ReverseGeocodingOptions {
            val argumentMap = arguments as Map<String, Any>

            var locale: Locale? = null
            val coordinate = Coordinate(
                argumentMap["latitude"] as Double,
                argumentMap["longitude"] as Double)

            if (argumentMap.containsKey("localeIdentifier")) {
                locale = LocaleConverter.fromLanguageTag(argumentMap["localeIdentifier"] as String)
            }

            return ReverseGeocodingOptions(coordinate, locale)
        }
    }
}
