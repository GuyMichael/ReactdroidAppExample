package com.guymichael.componentapplicationexample.store.datatype

import com.guymichael.componentapplicationexample.network.client.cats.data.CatData
import com.guymichael.componentapplicationexample.store.reducers.MainStoreNoPersistDataType


object DataTypeCats : MainStoreNoPersistDataType<CatData>() {
    override fun getSchemaId(d: CatData): String = d.id //a unique identifier
}