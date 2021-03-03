package com.guymichael.componentapplicationexample.exceptions

import android.app.Activity
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.BuildConfig
import com.guymichael.componentapplicationexample.exceptions.model.GlobalExceptionHandler
import com.guymichael.kotlinreact.Logger
import com.guymichael.reactdroid.core.Utils
import com.guymichael.reactdroid.extensions.navigation.AppForegroundLogic
import com.guymichael.reactiveapp.network.model.ApiError
import com.guymichael.reactiveapp.network.model.ApiResponseGeneralError
import com.guymichael.reactiveapp.utils.AndroidUtils
import io.reactivex.rxjava3.plugins.RxJavaPlugins

object GlobalErrorHandler {

    /**
     * Shows a toast on global rx errors
     */
    @JvmStatic
    fun initRxHandler() {
        //THINK remove this (how?). This is a global RxJava error handling, specifically used for Promise, in order to avoid 'Promise.all()'
        //THINK crashing when more than one zipped single is failing (throwing an error)
        //THINK see this issue here (and many more on the web) : https://github.com/ReactiveX/RxJava/issues/6249
        //THINK "zip can only deal with one of its sources failing.
        //THINK If multiple sources fail, RxJava can't tell if those other exceptions are significant for you and it can call onError again either.
        //THINK Thus it goes to the last resort handler as described in the wiki."
        RxJavaPlugins.setErrorHandler { e ->
            AppForegroundLogic.getForegroundActivity()?.let { foregroundActivity ->
                Utils.runOnUiThread {
                    AndroidUtils.toast(foregroundActivity,
                        if (BuildConfig.DEBUG) "RxJava global error handler: " + e.message //RxJava prepends the class path to the exception message. Remove that
                        else com.guymichael.reactiveapp.utils.TextUtils.splitAndTake(
                            e.message,
                            ": ",
                            2
                        ) ?: e.message ?: "No message"
                        ,
                        true
                    )

                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                }
            }

            Logger.w(GlobalErrorHandler::class, "RxJava global error handler: " + (e.message ?: "No message"))
        }
    }


    /**
     * Show general Toast on not-silent Api failures
     */
    @JvmStatic
    fun initPromiseHandler() {
        APromise.setGlobalAutoErrorHandler { context: Activity, e: Throwable ->
            val apiError: ApiError? = ApiError.parseOrNull(e)
            if (apiError == null) {
                if (BuildConfig.DEBUG) {
                    AndroidUtils.toast(context
                        , "some promise error (${e.javaClass.simpleName}), message: ${e.message}"
                        , true
                    )
                }
            } else {
                val apiMessage: String? = (apiError.errorBody as? ApiResponseGeneralError)?.message

                val msg: String = if (BuildConfig.DEBUG) {
                    "${apiError.message}, code: ${apiError.code}, message: $apiMessage"
                } else {
                    apiMessage ?: apiError.message
                }

                if (msg.isNotBlank()) {
                    AndroidUtils.toast(context, msg, true)
                }
            }
        }
    }

    @JvmStatic
    fun initDefaultUncaughtExceptionHandler() {
        // Register this service to handle uncaught Exceptions
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler())
    }
}