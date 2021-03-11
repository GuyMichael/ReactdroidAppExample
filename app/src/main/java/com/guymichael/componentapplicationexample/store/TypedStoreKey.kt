package com.guymichael.componentapplicationexample.store

import com.guymichael.componentapplicationexample.store.reducers.GeneralReducer
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinflux.model.StoreKey

interface TypedStoreKey<T> : StoreKey {
    override fun getName() = this.javaClass.simpleName
    override fun getReducer() = GeneralReducer

    @Suppress("UNCHECKED_CAST")
    fun get(state: GlobalState): T = getCurrentValue(state) as T //THINK unchecked cast

    @Suppress("UNCHECKED_CAST")
    fun getOrNull(state: GlobalState): T? = getCurrentValue(state) as? T? //THINK unchecked cast

    fun pairWith(that: T): Pair<StoreKey, T> = this to that
}