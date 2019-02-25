package com.baseflow.flutter.plugin.geolocator.tasks

import android.content.Context

import com.baseflow.flutter.plugin.geolocator.OnCompletionListener
import com.baseflow.flutter.plugin.geolocator.data.Result

import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

internal class TaskContext<TOptions> private constructor(
    val registrar: PluginRegistry.Registrar,
    val result: Result,
    val options: TOptions,
    val completionListener: OnCompletionListener) {

    val androidContext: Context
        get() = if (registrar.activity() != null) registrar.activity() else registrar.activeContext()

    companion object {

        fun <TOptions> buildForMethodResult(
            registrar: PluginRegistry.Registrar,
            methodResult: MethodChannel.Result,
            options: TOptions,
            completionListener: OnCompletionListener): TaskContext<TOptions> {
            val result = Result(methodResult)

            return TaskContext(
                registrar,
                result,
                options,
                completionListener)
        }

        fun <TOptions> buildForEventSink(
            registrar: PluginRegistry.Registrar,
            eventSink: EventChannel.EventSink,
            options: TOptions,
            completionListener: OnCompletionListener): TaskContext<TOptions> {
            val result = Result(eventSink)

            return TaskContext(
                registrar,
                result,
                options,
                completionListener)
        }
    }
}
