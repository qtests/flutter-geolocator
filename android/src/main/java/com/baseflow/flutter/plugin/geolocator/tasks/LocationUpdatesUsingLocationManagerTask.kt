package com.baseflow.flutter.plugin.geolocator.tasks

import android.location.*
import android.os.Bundle
import android.os.Looper
import com.baseflow.flutter.plugin.geolocator.data.GeolocationAccuracy
import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.baseflow.flutter.plugin.geolocator.data.PositionMapper
import com.google.android.gms.common.util.Strings

internal class LocationUpdatesUsingLocationManagerTask(context: TaskContext<LocationOptions>, private val mStopAfterFirstLocationUpdate: Boolean) : LocationUsingLocationManagerTask(context), LocationListener {
    private var bestLocation: Location? = null
    private var activeProvider: String? = null

    override fun startTask() {

        val locationManager = locationManager

        // Make sure we remove existing listeners before we register a new one
        locationManager.removeUpdates(this)

        // Try to get the best possible location provider for the requested accuracy
        activeProvider = getBestProvider(
            locationManager,
            mLocationOptions.accuracy)

        if (Strings.isEmptyOrWhitespace(activeProvider)) {
            handleError()

            return
        }

        bestLocation = locationManager.getLastKnownLocation(activeProvider)

        // If we are listening to multiple location updates we can go ahead
        // and report back the last known location (if we have one).
        val bestLocation = bestLocation
        if (!mStopAfterFirstLocationUpdate && bestLocation !=
            null) {
            reportLocationUpdate(bestLocation)
        }

        var looper = Looper.myLooper()
        if (looper == null) {
            looper = Looper.getMainLooper()
        }

        locationManager.requestLocationUpdates(
            activeProvider,
            mLocationOptions.timeInterval,
            mLocationOptions.distanceFilter.toFloat(),
            this,
            looper)
    }

    override fun stopTask() {
        super.stopTask()

        val locationManager = locationManager
        locationManager.removeUpdates(this)
    }

    private fun handleError() {
        taskContext.result.error(
            "INVALID_LOCATION_SETTINGS",
            "Location settings are inadequate, check your location settings.", null)
    }

    private fun getBestProvider(locationManager: LocationManager, accuracy: GeolocationAccuracy): String? {
        val criteria = Criteria()

        criteria.isBearingRequired = false
        criteria.isAltitudeRequired = false
        criteria.isSpeedRequired = false

        when (accuracy) {
            GeolocationAccuracy.LOWEST -> {
                criteria.accuracy = Criteria.NO_REQUIREMENT
                criteria.horizontalAccuracy = Criteria.NO_REQUIREMENT
                criteria.powerRequirement = Criteria.NO_REQUIREMENT
            }
            GeolocationAccuracy.LOW -> {
                criteria.accuracy = Criteria.ACCURACY_COARSE
                criteria.horizontalAccuracy = Criteria.ACCURACY_LOW
                criteria.powerRequirement = Criteria.NO_REQUIREMENT
            }
            GeolocationAccuracy.MEDIUM -> {
                criteria.accuracy = Criteria.ACCURACY_COARSE
                criteria.horizontalAccuracy = Criteria.ACCURACY_MEDIUM
                criteria.powerRequirement = Criteria.POWER_MEDIUM
            }
            GeolocationAccuracy.HIGH -> {
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                criteria.powerRequirement = Criteria.POWER_HIGH
            }
            GeolocationAccuracy.BEST -> {
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                criteria.powerRequirement = Criteria.POWER_HIGH
            }
        }

        var provider = locationManager.getBestProvider(criteria, true)

        if (Strings.isEmptyOrWhitespace(provider)) {
            val providers = locationManager.getProviders(true)
            if (providers != null && providers.size > 0)
                provider = providers[0]
        }

        return provider
    }

    override fun onLocationChanged(location: Location) {
        val desiredAccuracy = accuracyToFloat(mLocationOptions.accuracy)
        if (LocationUsingLocationManagerTask.isBetterLocation(location, bestLocation) && location.accuracy <= desiredAccuracy) {
            bestLocation = location
            reportLocationUpdate(location)

            if (mStopAfterFirstLocationUpdate) {
                this.stopTask()
            }
        }
    }

    override fun onStatusChanged(provider: String, status: Int, bundle: Bundle) {
        if (status == LocationProvider.AVAILABLE) {
            onProviderEnabled(provider)
        } else if (status == LocationProvider.OUT_OF_SERVICE) {
            onProviderDisabled(provider)
        }

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {
        if (provider == activeProvider) {
            taskContext.result.error(
                "ERROR_UPDATING_LOCATION",
                "The active location provider was disabled. Check if the location services are enabled in the device settings.", null)
        }
    }

    private fun accuracyToFloat(accuracy: GeolocationAccuracy): Float {
        return when (accuracy) {
            GeolocationAccuracy.LOWEST, GeolocationAccuracy.LOW -> 500f
            GeolocationAccuracy.MEDIUM -> 250f
            GeolocationAccuracy.HIGH -> 100f
            GeolocationAccuracy.BEST -> 50f
        }
    }

    private fun reportLocationUpdate(location: Location) {
        val locationMap = PositionMapper.toHashMap(location)

        taskContext.result.success(locationMap)
    }
}
