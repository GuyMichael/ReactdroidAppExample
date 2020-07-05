package com.guymichael.componentapplicationexample

import com.guymichael.componentapplicationexample.exceptions.GlobalErrorHandler
import com.guymichael.componentapplicationexample.navigation.CLIENT_PAGE
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.reactiveapp.BaseApplication
import com.guymichael.reactiveapp.network.model.ApiClient
import com.guymichael.reactdroid.core.ReactdroidLogger
import com.guymichael.reactdroid.extensions.navigation.AppForegroundLogic
import com.guymichael.reactdroid.extensions.router.Router
import io.objectbox.BoxStore
import retrofit2.converter.moshi.MoshiConverterFactory

class MyApplication : BaseApplication() {

    override fun initErrorHandling() {
        GlobalErrorHandler.initDefaultUncaughtExceptionHandler()
        GlobalErrorHandler.initRxHandler()
        GlobalErrorHandler.initPromiseHandler()
    }

    override fun initLogging() {
        ReactdroidLogger.enableLogging()
        com.guymichael.promise.Logger.init(ReactdroidLogger())
        AppForegroundLogic.logLifecycle()
    }

    override fun initDb(): BoxStore {
        return ApplicationStartupLogic.createBoxStore(this)
    }

    override fun initStore() {
        MainStore //singleton
    }

    override fun initDeepLink() {
        Router.init(CLIENT_PAGE.Companion::parse)
    }

    override fun initApiClients(defaultJsonParser: MoshiConverterFactory): List<ApiClient> {
        return ApplicationStartupLogic.createApiClients(defaultJsonParser)
    }
}