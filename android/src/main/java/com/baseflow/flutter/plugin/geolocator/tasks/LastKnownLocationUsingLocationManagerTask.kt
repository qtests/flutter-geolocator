package com.baseflow.flutter.plugin.geolocator.tasks

import android.location.Location
import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.baseflow.flutter.plugin.geolocator.data.PositionMapper

internal class LastKnownLocationUsingLocationManagerTask(context: TaskContext<LocationOptions>) : LocationUsingLocationManagerTask(context) {

    override fun startTask() {
        val locationManager = locationManager

        var bestLocation: Location? = null

        for (provider in locationManager.getProviders(true)) {
            val location = locationManager.getLastKnownLocation(provider)

            if (location != null && LocationUsingLocationManagerTask.isBetterLocation(location, bestLocation)) {
                bestLocation = location
            }
        }

        val result = taskContext.result
        if (bestLocation == null) {
            result.success(null)
            stopTask()
            return
        }

        result.success(PositionMapper.toHashMap(bestLocation))
        stopTask()
    }
}
