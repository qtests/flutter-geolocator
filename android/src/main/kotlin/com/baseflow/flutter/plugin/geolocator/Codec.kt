package com.baseflow.flutter.plugin.geolocator

import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.google.gson.GsonBuilder

object Codec {
    private val GSON_DECODER = GsonBuilder().enableComplexMapKeySerialization().create()


    fun decodeLocationOptions(arguments: Any): LocationOptions {
        return GSON_DECODER.fromJson(arguments as String, LocationOptions::class.java)
    }
}
