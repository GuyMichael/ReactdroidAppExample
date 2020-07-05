package com.guymichael.componentapplicationexample.network.client.translations.response

import com.guymichael.componentapplicationexample.network.client.translations.data.TranslationData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseTranslations(
    val outputs: List<TranslationData>,
    val message: String? = null
)