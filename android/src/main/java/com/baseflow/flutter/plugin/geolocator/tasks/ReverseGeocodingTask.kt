package com.baseflow.flutter.plugin.geolocator.tasks

import android.content.Context
import android.location.Geocoder
import com.baseflow.flutter.plugin.geolocator.data.AddressMapper
import com.baseflow.flutter.plugin.geolocator.data.Coordinate
import com.baseflow.flutter.plugin.geolocator.data.ReverseGeocodingOptions
import java.io.IOException
import java.util.*

internal class ReverseGeocodingTask(context: TaskContext<ReverseGeocodingOptions>) : Task<ReverseGeocodingOptions>(context) {
    private val mAndroidContext: Context

    private val mCoordinatesToLookup: Coordinate
    private val mLocale: Locale?

    init {

        val registrar = context.registrar

        mAndroidContext = if (registrar.activity() !=
            null) registrar.activity() else registrar.activeContext()
        mCoordinatesToLookup = context.options.coordinate
        mLocale = context.options.locale
    }

    override fun startTask() {
        val geocoder = mLocale?.let { Geocoder(mAndroidContext, it) } ?: Geocoder(mAndroidContext)

        val result = taskContext.result

        try {
            val addresses = geocoder.getFromLocation(mCoordinatesToLookup.latitude, mCoordinatesToLookup.longitude, 1)

            if (addresses.size > 0) {
                result.success(AddressMapper.toHashMapList(addresses))
            } else {
                result.error(
                    "ERROR_GEOCODING_INVALID_COORDINATES",
                    "Unable to find an address for the supplied coordinates.", null)
            }

        } catch (e: IOException) {
            result.error(
                "ERROR_GEOCODING_COORDINATES",
                e.localizedMessage, null)
        } finally {
            stopTask()
        }
    }
}
