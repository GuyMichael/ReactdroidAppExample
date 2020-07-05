package com.guymichael.componentapplicationexample.store

import com.guymichael.componentapplicationexample.store.reducers.GeneralReducer
import com.guymichael.componentapplicationexample.store.reducers.InputSheetReducer
import com.guymichael.componentapplicationexample.store.reducers.MainDataReducer
import com.guymichael.reactdroidflux.model.AndroidStore

object MainStore : AndroidStore(combineReducers(
    MainDataReducer, GeneralReducer, InputSheetReducer
))