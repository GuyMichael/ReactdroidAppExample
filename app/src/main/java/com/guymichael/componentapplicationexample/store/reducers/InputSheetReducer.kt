package com.guymichael.componentapplicationexample.store.reducers

import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinflux.model.StoreKey
import com.guymichael.kotlinflux.model.reducers.Reducer

object InputSheetReducer : Reducer() {
    override fun getSelfDefaultState() = GlobalState()
    //we're used to consider null Boolean as `false` so no explicit default value needed
}

enum class InputSheetReducerKey : StoreKey {
    checkbox1_checked,
    checkbox2_checked,
    checkbox3_checked,

    radio_checked_id,

    toggle_checked_1,
    toggle_checked_2,
    toggle_checked_3,

    switch_checked_1,
    switch_checked_2,
    switch_checked_3,

    seekbar1_progress,
    seekbar2_progress,
    seekbar3_progress,
    ;

    override fun getName() = this.name
    override fun getReducer() = InputSheetReducer
}