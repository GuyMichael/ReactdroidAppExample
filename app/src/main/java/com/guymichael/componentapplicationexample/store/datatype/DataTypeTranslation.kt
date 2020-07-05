package com.guymichael.componentapplicationexample.store.datatype

import com.guymichael.componentapplicationexample.network.client.translations.data.TranslationData
import com.guymichael.componentapplicationexample.store.reducers.MainStoreNoPersistDataType


object DataTypeTranslation : MainStoreNoPersistDataType<TranslationData>() {
    override fun getSchemaId(d: TranslationData): String = d.output //a unique identifier
}