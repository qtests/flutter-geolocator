package com.baseflow.flutter.plugin.geolocator.tasks

import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.baseflow.flutter.plugin.geolocator.data.PositionMapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


internal class LastKnownLocationUsingLocationServicesTask(taskContext: TaskContext<LocationOptions>) : LocationUsingLocationServicesTask(taskContext) {
    private val mFusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(taskContext.androidContext)

    override fun startTask() {
        mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                val locationMap = if (location !=
                    null)
                    PositionMapper.toHashMap(location)
                else
                    null

                taskContext.result.success(locationMap)

                stopTask()
            }
            .addOnFailureListener { e ->
                taskContext.result.error(
                    e.message ?: "Error",
                    e.localizedMessage, null)

                stopTask()
            }
    }
}
