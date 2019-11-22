package com.baseflow.flutter.plugin.geolocator.tasks

import java.util.*

abstract class Task<TOptions> internal constructor(internal val taskContext: TaskContext<TOptions>) {
    val taskID: UUID

    init {
        taskID = UUID.randomUUID()
    }

    abstract fun startTask()

    open fun stopTask() {
        val completionListener = taskContext.completionListener

        completionListener.onCompletion(taskID)
    }
}
