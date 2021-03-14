package com.guymichael.componentapplicationexample.store.reducers

import com.guymichael.componentapplicationexample.store.TypedStoreKey
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinflux.model.reducers.Reducer

/**
 * A Reducer example which uses sealed-class StoreKeys (instead of an enum),
 * which allows for typed keys
 */
object GeneralReducer : Reducer() {
    override fun getSelfDefaultState() = GlobalState(
        WelcomeDialogShown.pairWith(true)
    )
}

sealed class GeneralReducerKey<T> : TypedStoreKey<T> {
    override fun getName() = this.javaClass.simpleName
    override fun getReducer() = GeneralReducer
}
/* define all the (sealed) keys */
object WelcomeDialogShown : GeneralReducerKey<Boolean>()
//note: this usage enforces the state to include a non-null Boolean, meaning you must
//      provide an initial value inside getSelfDefaultState().
//      Otherwise, we could use GeneralReducerKey<Boolean?>() to allow nullable values