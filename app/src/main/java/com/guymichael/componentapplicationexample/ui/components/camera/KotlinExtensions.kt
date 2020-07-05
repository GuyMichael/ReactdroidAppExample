package com.guymichael.componentapplicationexample.ui.components.camera

import com.google.common.util.concurrent.ListenableFuture
import com.guymichael.apromise.APromise

fun <T> APromise.Companion.from(future: ListenableFuture<T>) : APromise<T> {
    return ofCallback { promiseCallback ->
        future.addListener(
            { promiseCallback.onSuccess(future.get()) }
            , { it.run() }
        )
    }
}