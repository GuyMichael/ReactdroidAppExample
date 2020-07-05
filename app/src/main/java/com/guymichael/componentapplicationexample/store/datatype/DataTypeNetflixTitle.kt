package com.guymichael.componentapplicationexample.store.datatype

import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixTitleData
import com.guymichael.componentapplicationexample.store.reducers.MainStoreNoPersistDataType


object DataTypeNetflixTitle : MainStoreNoPersistDataType<NetflixTitleData>() {
    override fun getSchemaId(d: NetflixTitleData): String = d.netflixid.toString() //a unique identifier
}