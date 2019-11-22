package com.baseflow.flutter.plugin.geolocator.data

import android.location.Location
import android.os.Build
import java.util.*

object PositionMapper {

    fun toHashMap(location: Location): Map<String, Any> {
        val position = HashMap<String, Any>()

        position["latitude"] = location.latitude
        position["longitude"] = location.longitude
        position["timestamp"] = location.time

        if (location.hasAltitude())
            position["altitude"] = location.altitude
        if (location.hasAccuracy())
            position["accuracy"] = location.accuracy.toDouble()
        if (location.hasBearing())
            position["heading"] = location.bearing.toDouble()
        if (location.hasSpeed())
            position["speed"] = location.speed.toDouble()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasSpeedAccuracy())
            position["speed_accuracy"] = location.speedAccuracyMetersPerSecond.toDouble()

        return position
    }
}
