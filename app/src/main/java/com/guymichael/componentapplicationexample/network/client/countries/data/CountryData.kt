package com.guymichael.componentapplicationexample.network.client.countries.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryData(
    val name: String,
    val region: String?,
    val capital: String,
    val nativeName: String?,
    val population: Long?
)