package com.baseflow.flutter.plugin.geolocator.data

import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class Result {
    private val mMethodResult: MethodChannel.Result?
    private val mEventResult: EventChannel.EventSink?

    constructor(methodResult: MethodChannel.Result) {
        mEventResult = null
        mMethodResult = methodResult
    }

    constructor(eventResult: EventChannel.EventSink) {
        mEventResult = eventResult
        mMethodResult = null
    }

    fun success(o: Any?) {
        if (mMethodResult != null) {
            mMethodResult.success(o)
        } else {
            mEventResult!!.success(o)
        }
    }

    fun error(code: String, message: String, details: Any?) {
        if (mMethodResult != null) {
            mMethodResult.error(code, message, details)
        } else {
            mEventResult!!.error(code, message, details)
        }
    }
}
