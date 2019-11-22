package com.baseflow.flutter.plugin.geolocator.tasks

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.baseflow.flutter.plugin.geolocator.data.LocationOptions

internal abstract class LocationUsingLocationManagerTask(context: TaskContext<LocationOptions>) : Task<LocationOptions>(context) {

    private val mAndroidContext: Context
    val mLocationOptions: LocationOptions

    val locationManager: LocationManager
        get() = mAndroidContext.getSystemService(Activity.LOCATION_SERVICE) as LocationManager

    init {

        val registrar = context.registrar

        mAndroidContext = if (registrar.activity() != null) registrar.activity() else registrar.activeContext()
        mLocationOptions = context.options
    }

    abstract override fun startTask()

    companion object {
        private const val TWO_MINUTES: Long = 120000

        fun isBetterLocation(location: Location, bestLocation: Location?): Boolean {
            if (bestLocation == null)
                return true

            val timeDelta = location.time - bestLocation.time

            val isSignificantlyNewer = timeDelta > TWO_MINUTES
            val isSignificantlyOlder = timeDelta < -TWO_MINUTES
            val isNewer = timeDelta > 0

            if (isSignificantlyNewer)
                return true

            if (isSignificantlyOlder)
                return false

            val accuracyDelta = (location.accuracy - bestLocation.accuracy).toInt().toFloat()
            val isLessAccurate = accuracyDelta > 0
            val isMoreAccurate = accuracyDelta < 0
            val isSignificantlyLessAccurate = accuracyDelta > 200

            var isFromSameProvider = false
            if (location.provider != null) {
                isFromSameProvider = location.provider == bestLocation.provider
            }

            if (isMoreAccurate)
                return true

            if (isNewer && !isLessAccurate)
                return true


            return isNewer && !isSignificantlyLessAccurate && isFromSameProvider

        }
    }
}
