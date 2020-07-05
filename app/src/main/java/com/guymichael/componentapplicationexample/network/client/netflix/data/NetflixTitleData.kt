package com.guymichael.componentapplicationexample.network.client.netflix.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetflixTitleData(
    val netflixid: Long,
    val title: String,
    val type: String,
    val runtime: String,
    val released: String,
    val image: String,
    val rating: Float,
    val imdbid: String
)