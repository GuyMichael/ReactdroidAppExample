package com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.guymichael.kotlinflux.model.StoreObserver
import com.guymichael.kotlinreact.Logger
import com.guymichael.kotlinreact.model.OwnProps

class LifecycleBoundObserver<P : OwnProps>(
    private val mOwner: LifecycleOwner
    , observer: StoreObserver<P>
    , private val unsubscribeObserver: (ObserverWrapper<P>) -> Unit
) : ObserverWrapper<P>(observer), LifecycleEventObserver {

    override fun shouldBeActive(): Boolean {
        return mObserver.shouldReceiveStateChanges()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Logger.d(
            "ComposeConnect",
            "LifecycleBoundObserver#onStateChanged(${event.name})"
        ) //NOCOMMIT log
        var currentState: Lifecycle.State = mOwner.lifecycle.currentState
        if (currentState === Lifecycle.State.DESTROYED) {
            unsubscribeObserver(this)
            return
        }
        var prevState: Lifecycle.State? = null
        while (prevState !== currentState) {
            prevState = currentState
            activeStateChanged(shouldBeActive())
            currentState = mOwner.lifecycle.currentState
        }
    }

    override fun isAttachedTo(owner: LifecycleOwner): Boolean {
        return mOwner === owner
    }

    override fun detachObserver() {
        mOwner.lifecycle.removeObserver(this)
    }
}