package com.guymichael.componentapplicationexample.exceptions.model

import android.util.Log

class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
    private val defaultUEH: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        Log.e(GlobalExceptionHandler::class.java.simpleName, "Caught Exception: " + exception.message)
        exception.printStackTrace()

        //forward to original handler for the app to actually crash
        defaultUEH?.uncaughtException(thread, exception)
    }
}