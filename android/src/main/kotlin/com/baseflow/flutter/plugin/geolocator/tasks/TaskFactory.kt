package com.baseflow.flutter.plugin.geolocator.tasks

import com.baseflow.flutter.plugin.geolocator.OnCompletionListener
import com.baseflow.flutter.plugin.geolocator.data.CalculateDistanceOptions
import com.baseflow.flutter.plugin.geolocator.data.ForwardGeocodingOptions
import com.baseflow.flutter.plugin.geolocator.data.LocationOptions
import com.baseflow.flutter.plugin.geolocator.data.ReverseGeocodingOptions

import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

object TaskFactory {
    fun createCalculateDistanceTask(
        registrar: PluginRegistry.Registrar,
        result: MethodChannel.Result,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = CalculateDistanceOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForMethodResult(
            registrar,
            result,
            options,
            completionListener)

        return CalculateDistanceTask(taskContext)
    }

    fun createCurrentLocationTask(
        registrar: PluginRegistry.Registrar,
        result: MethodChannel.Result,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = LocationOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForMethodResult(
            registrar,
            result,
            options,
            completionListener)

        return if (!options.forceAndroidLocationManager) {
            LocationUpdatesUsingLocationServicesTask(
                taskContext,
                true)
        } else {
            LocationUpdatesUsingLocationManagerTask(
                taskContext,
                true)
        }
    }

    fun createForwardGeocodingTask(
        registrar: PluginRegistry.Registrar,
        result: MethodChannel.Result,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = ForwardGeocodingOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForMethodResult(
            registrar,
            result,
            options,
            completionListener)

        return ForwardGeocodingTask(taskContext)
    }

    fun createLastKnownLocationTask(
        registrar: PluginRegistry.Registrar,
        result: MethodChannel.Result,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = LocationOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForMethodResult(
            registrar,
            result,
            options,
            completionListener)

        return if (!options.forceAndroidLocationManager) {
            LastKnownLocationUsingLocationServicesTask(taskContext)
        } else {
            LastKnownLocationUsingLocationManagerTask(taskContext)
        }
    }

    fun createReverseGeocodingTask(
        registrar: PluginRegistry.Registrar,
        result: MethodChannel.Result,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = ReverseGeocodingOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForMethodResult(
            registrar,
            result,
            options,
            completionListener)

        return ReverseGeocodingTask(taskContext)
    }

    fun createStreamLocationUpdatesTask(
        registrar: PluginRegistry.Registrar,
        result: EventChannel.EventSink,
        arguments: Any,
        completionListener: OnCompletionListener): Task<*> {

        val options = LocationOptions.parseArguments(arguments)
        val taskContext = TaskContext.buildForEventSink(
            registrar,
            result,
            options,
            completionListener)

        return if (!options.forceAndroidLocationManager) {
            LocationUpdatesUsingLocationServicesTask(
                taskContext,
                false)
        } else {
            LocationUpdatesUsingLocationManagerTask(
                taskContext,
                false)
        }
    }
}
