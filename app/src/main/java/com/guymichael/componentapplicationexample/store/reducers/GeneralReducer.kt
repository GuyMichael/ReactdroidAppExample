package com.guymichael.componentapplicationexample.store.reducers

import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinflux.model.StoreKey
import com.guymichael.kotlinflux.model.reducers.Reducer

object GeneralReducer : Reducer() {
    override fun getSelfDefaultState() = GlobalState(
        GeneralReducerKey.welcomeDialogShown to true
    )
}

enum class GeneralReducerKey : StoreKey {
    welcomeDialogShown
    ;

    override fun getName() = this.name
    override fun getReducer() = GeneralReducer
}