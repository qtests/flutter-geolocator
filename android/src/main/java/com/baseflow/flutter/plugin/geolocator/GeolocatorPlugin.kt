package com.baseflow.flutter.plugin.geolocator

import com.baseflow.flutter.plugin.geolocator.tasks.Task
import com.baseflow.flutter.plugin.geolocator.tasks.TaskFactory
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

/**
 * GeolocatorPlugin
 */
class GeolocatorPlugin private constructor(registrar: PluginRegistry.Registrar) : MethodCallHandler, EventChannel.StreamHandler, OnCompletionListener {

    // mTasks is used to track active tasks, when tasks completes it is removed from the map
    private val mTasks = HashMap<UUID, Task<*>>()
    private val mRegistrar: Registrar = registrar
    private var mStreamLocationTask: Task<*>? = null

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getLastKnownPosition" -> {
                val task = TaskFactory.createLastKnownLocationTask(
                    mRegistrar, result, call.arguments, this)
                mTasks[task.taskID] = task
                task.startTask()
            }
            "getCurrentPosition" -> {
                val task = TaskFactory.createCurrentLocationTask(
                    mRegistrar, result, call.arguments, this)
                mTasks[task.taskID] = task
                task.startTask()
            }
            "placemarkFromAddress" -> {
                val task = TaskFactory.createForwardGeocodingTask(
                    mRegistrar, result, call.arguments, this)
                mTasks[task.taskID] = task
                task.startTask()
            }
            "placemarkFromCoordinates" -> {
                val task = TaskFactory.createReverseGeocodingTask(
                    mRegistrar, result, call.arguments, this)
                mTasks[task.taskID] = task
                task.startTask()
            }
            "distanceBetween" -> {
                val task = TaskFactory.createCalculateDistanceTask(
                    mRegistrar, result, call.arguments, this)
                mTasks[task.taskID] = task
                task.startTask()
            }
            else -> result.notImplemented()
        }
    }

    override fun onListen(o: Any, eventSink: EventChannel.EventSink) {
        if (mStreamLocationTask != null) {
            eventSink.error(
                "ALREADY_LISTENING",
                "You are already listening for location changes. Create a new instance or stop listening to the current stream.", null)

            return
        }

        mStreamLocationTask = TaskFactory.createStreamLocationUpdatesTask(
            mRegistrar, eventSink, o, this)
        mStreamLocationTask!!.startTask()
    }

    override fun onCancel(arguments: Any) {
        if (mStreamLocationTask == null) return

        mStreamLocationTask!!.stopTask()
        mStreamLocationTask = null
    }

    override fun onCompletion(taskID: UUID) {
        mTasks.remove(taskID)
    }

    companion object {
        private const val METHOD_CHANNEL_NAME = "flutter.baseflow.com/geolocator/methods"
        private const val EVENT_CHANNEL_NAME = "flutter.baseflow.com/geolocator/events"


        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val geolocatorPlugin = GeolocatorPlugin(registrar)

            val methodChannel = MethodChannel(registrar.messenger(), METHOD_CHANNEL_NAME)
            val eventChannel = EventChannel(registrar.messenger(), EVENT_CHANNEL_NAME)
            methodChannel.setMethodCallHandler(geolocatorPlugin)
            eventChannel.setStreamHandler(geolocatorPlugin)
        }
    }


}
