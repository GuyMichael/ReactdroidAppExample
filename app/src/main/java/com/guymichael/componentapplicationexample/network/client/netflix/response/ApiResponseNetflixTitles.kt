package com.guymichael.componentapplicationexample.network.client.netflix.response

import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixTitleData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseNetflixTitles(
    val ITEMS: List<NetflixTitleData>,
    val COUNT: String
)