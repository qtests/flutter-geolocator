package com.baseflow.flutter.plugin.geolocator.data

import com.baseflow.flutter.plugin.geolocator.Codec

class LocationOptions {
    var accuracy = GeolocationAccuracy.BEST
    var distanceFilter: Long = 0
    var forceAndroidLocationManager = false
    var timeInterval: Long = 0

    companion object {

        fun parseArguments(arguments: Any): LocationOptions {
            return Codec.decodeLocationOptions(arguments)
        }
    }
}
