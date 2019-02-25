package com.baseflow.flutter.plugin.geolocator.tasks

import android.location.Location
import com.baseflow.flutter.plugin.geolocator.data.CalculateDistanceOptions

internal class CalculateDistanceTask(context: TaskContext<CalculateDistanceOptions>) : Task<CalculateDistanceOptions>(context) {

    override fun startTask() {
        val flutterResult = taskContext.result
        val options = taskContext.options

        if (options.sourceCoordinates ==
            null || options.destinationCoordinates == null) {
            flutterResult.error(
                "ERROR_CALCULATE_DISTANCE_INVALID_PARAMS",
                "Please supply start and end coordinates.", null)
        }

        val results = FloatArray(1)

        try {
            Location.distanceBetween(
                options.sourceCoordinates.latitude,
                options.sourceCoordinates.longitude,
                options.destinationCoordinates.latitude,
                options.destinationCoordinates.longitude,
                results)

            // According to the Android documentation the distance is
            // always stored in the first position of the result array
            // (see: https://developer.android.com/reference/android/location/Location.html#distanceBetween(double,%20double,%20double,%20double,%20float[]))
            flutterResult.success(results[0])
        } catch (ex: IllegalArgumentException) {
            flutterResult.error(
                "ERROR_CALCULATE_DISTANCE_ILLEGAL_ARGUMENT",
                ex.localizedMessage, null)
        } finally {
            stopTask()
        }

    }
}
