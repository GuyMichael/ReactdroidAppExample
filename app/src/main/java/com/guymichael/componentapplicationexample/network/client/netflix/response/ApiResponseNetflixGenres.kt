package com.guymichael.componentapplicationexample.network.client.netflix.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseNetflixGenres(
    val ITEMS: List<Map<String, List<Double>>>,
    val COUNT: String
)