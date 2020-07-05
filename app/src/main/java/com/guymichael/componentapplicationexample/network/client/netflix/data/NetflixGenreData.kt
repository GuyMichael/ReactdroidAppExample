package com.guymichael.componentapplicationexample.network.client.netflix.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetflixGenreData(
    val name: String,
    val titleIds: List<Long>
)