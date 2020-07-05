package com.guymichael.componentapplicationexample.store.datatype

import com.guymichael.componentapplicationexample.network.client.countries.data.CountryData
import com.guymichael.componentapplicationexample.store.reducers.MainStoreNoPersistDataType


object DataTypeCountry : MainStoreNoPersistDataType<CountryData>() {
    override fun getSchemaId(d: CountryData): String = d.name //a unique identifier
}