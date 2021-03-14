package com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core

import androidx.lifecycle.LifecycleOwner
import com.guymichael.kotlinflux.model.StoreObserver
import com.guymichael.kotlinreact.model.OwnProps

abstract class ObserverWrapper<P : OwnProps>(val mObserver: StoreObserver<P>) {
    private var mActive = false
    abstract fun shouldBeActive(): Boolean

    open fun isAttachedTo(owner: LifecycleOwner): Boolean {
        return false
    }

    open fun detachObserver() {}

    fun activeStateChanged(newActive: Boolean) {
        if (newActive == mActive) {
            return
        }
        // immediately set active state, so we'd never dispatch anything to inactive
        // owner
        mActive = newActive

        //THINK consider enabling to notify on 'view got alive'
        // probably isn't necessary as the Composable will be called again with props
        /*if (mActive) {
            val nextProps = mObserver.mapStateToProps(storeSupplier().state)
            if (mObserver.shouldUpdate(prevProps, nextProps)) {
                mObserver.onStoreStateChanged(nextProps)
            }
        }*/
    }
}