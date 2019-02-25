package com.baseflow.flutter.plugin.geolocator.tasks

import android.location.Location
import android.os.Looper
import com.baseflow.flutter.plugin.geolocator.data.GeolocationAccuracy
import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.baseflow.flutter.plugin.geolocator.data.PositionMapper
import com.google.android.gms.location.*


internal class LocationUpdatesUsingLocationServicesTask(taskContext: TaskContext<LocationOptions>, private val mStopAfterFirstLocationUpdate: Boolean) : LocationUsingLocationServicesTask(taskContext) {
    private val mFusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(taskContext.androidContext)

    private val mLocationCallback: LocationCallback


    init {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }

                for (location in locationResult.locations) {
                    if (location != null) {
                        reportLocationUpdate(location)

                        if (mStopAfterFirstLocationUpdate) {
                            break
                        }
                    }
                }

                if (mStopAfterFirstLocationUpdate) {
                    stopTask()
                }
            }
        }
    }

    override fun startTask() {
        // Make sure we remove existing callbacks before we add a new one
        mFusedLocationProviderClient
            .removeLocationUpdates(mLocationCallback)
            .addOnCompleteListener {
                var looper = Looper.myLooper()
                if (looper == null) {
                    looper = Looper.getMainLooper()
                }

                mFusedLocationProviderClient.requestLocationUpdates(
                    createLocationRequest(),
                    mLocationCallback,
                    looper)
            }
    }

    override fun stopTask() {
        super.stopTask()

        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()

        locationRequest
            .setInterval(locationOptions.timeInterval)
            .setFastestInterval(locationOptions.timeInterval).smallestDisplacement = locationOptions.distanceFilter.toFloat()

        when (locationOptions.accuracy) {
            GeolocationAccuracy.LOW, GeolocationAccuracy.LOWEST -> locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
            GeolocationAccuracy.MEDIUM -> locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            GeolocationAccuracy.HIGH, GeolocationAccuracy.BEST -> locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return locationRequest
    }

    private fun reportLocationUpdate(location: Location?) {
        val locationMap = PositionMapper.toHashMap(location!!)

        taskContext.result.success(locationMap)
    }
}
