package com.guymichael.componentapplicationexample.store.reducers

import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.kotlinflux.extensions.data.model.DataReducer
import com.guymichael.kotlinflux.extensions.data.model.StoreDataType
import com.guymichael.kotlinflux.extensions.data.model.StoreDataTypeSingleModel

object MainDataReducer : DataReducer() {
    override fun getDefaultStatePersistenceTypes() = emptyList<StoreDataType<*>>()
//    listOf(
        //we leave this one out because we don't want to load all Genre records from db when app starts.
        // Instead, we will do it on first fetch in NetflixLogic.loadOrFetchGenres()
//        , DataTypeNetflixGenre


        //we leave these out as we know they don't have persistence
//        DataTypeTranslation
//        , DataTypeCountry
//        , DataTypeNetflixTitle
//    )
}

abstract class MainStoreDataType<T : Any> : StoreDataType<T>() {
    override fun getStore() =
        MainStore
    override fun getReducer() =
        MainDataReducer
}

abstract class MainStoreSingleDataType<T : Any> : StoreDataTypeSingleModel<T>() {
    override fun getStore() =
        MainStore
    override fun getReducer() =
        MainDataReducer
}

abstract class MainStoreNoPersistDataType<T : Any> : MainStoreDataType<T>() {
    override fun persistOrThrow(data: List<T>) {}
    override fun getPersistedData(): Nothing? = null
    override fun removeFromPersistOrThrow(data: List<T>) {}
    override fun clearPersistOrThrow() {}

    override fun dispatchLoaded(data: List<T>, merge: Boolean, shouldPersist: Boolean) {
        super.dispatchLoaded(data, merge, false)
    }
}