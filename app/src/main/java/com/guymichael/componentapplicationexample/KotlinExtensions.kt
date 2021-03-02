package com.guymichael.componentapplicationexample

import com.guymichael.apromise.APromise
import com.guymichael.reactdroid.core.Utils
import com.guymichael.reactdroid.core.letIf
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.withAutoCancel
import com.guymichael.reactdroid.core.withGlobalErrorHandling
import com.guymichael.reactiveapp.activities.BaseActivity
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.ref.WeakReference

fun AComponent<*, *, *>.showBlockUiProgress() {
    mView.context?.also {
        Utils.getActivity(it, BaseActivity::class.java)?.showBlockUiProgress()
    }
}

fun AComponent<*, *, *>.dismissProgress() {
    mView.context?.also {
        Utils.getActivity(it, BaseActivity::class.java)?.dismissProgress()
    }
}

fun <T> APromise<T>.withBlockUiProgress(component: AComponent<*, *, *>): APromise<T> {
    val componentRef = WeakReference(component)

    return doOnExecution { componentRef.get()?.showBlockUiProgress() }
        .finally { componentRef.get()?.dismissProgress() }
}

fun <T> APromise<T>.withBlockUiProgress(context: BaseActivity<*, *, *>): APromise<T> {
    val contextRef = WeakReference(context)

    return doOnExecution { contextRef.get()?.showBlockUiProgress() }
        .finally { contextRef.get()?.dismissProgress() }
}

fun APromise<*>.execute(component: AComponent<*, *, *>
        , autoCancel: Boolean = false
        , withBlockUiProgress: Boolean = false
        , handleErrorMessage: Boolean = true
    ): Disposable {

    return this
        .letIf({autoCancel}) {
            it.withAutoCancel(component)
        }
        .letIf({withBlockUiProgress}) {
            it.withBlockUiProgress(component)
        }
        .letIf({handleErrorMessage}) {
            it.withGlobalErrorHandling(component)
        }
        .execute()
}