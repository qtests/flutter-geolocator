package com.baseflow.flutter.plugin.geolocator.tasks

import com.baseflow.flutter.plugin.geolocator.data.LocationOptions

internal abstract class LocationUsingLocationServicesTask(taskContext: TaskContext<LocationOptions>) : Task<LocationOptions>(taskContext) {
    val locationOptions: LocationOptions = taskContext.options

}
