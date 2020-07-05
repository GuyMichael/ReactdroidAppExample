package com.guymichael.componentapplicationexample.network.client.translations.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslationData(
    val output: String
)