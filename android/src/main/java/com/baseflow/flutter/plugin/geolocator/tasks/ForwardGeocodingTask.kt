package com.baseflow.flutter.plugin.geolocator.tasks

import android.content.Context
import android.location.Geocoder
import com.baseflow.flutter.plugin.geolocator.data.AddressMapper
import com.baseflow.flutter.plugin.geolocator.data.ForwardGeocodingOptions
import java.io.IOException

internal class ForwardGeocodingTask(context: TaskContext<ForwardGeocodingOptions>) : Task<ForwardGeocodingOptions>(context) {
    private val mContext: Context

    init {

        val registrar = context.registrar

        mContext = if (registrar.activity() !=
            null) registrar.activity() else registrar.activeContext()
    }

    override fun startTask() {
        val options = taskContext.options

        val geocoder = if (options.locale != null)
            Geocoder(mContext, options.locale)
        else
            Geocoder(mContext)

        val result = taskContext.result

        try {
            val addresses = geocoder.getFromLocationName(options.addressToLookup, 1)

            if (addresses.size > 0) {
                result.success(AddressMapper.toHashMapList(addresses))
            } else {
                result.error(
                    "ERROR_GEOCODNG_ADDRESSNOTFOUND",
                    "Unable to find coordinates matching the supplied address.", null)
            }

        } catch (e: IOException) {
            result.error(
                "ERROR_GEOCODING_ADDRESS",
                e.localizedMessage, null)
        } finally {
            stopTask()
        }
    }
}
