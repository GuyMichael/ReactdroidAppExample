package com.guymichael.componentapplicationexample.network.client.cats.request

import com.guymichael.componentapplicationexample.network.client.cats.data.CatData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiCatsGet {

    @GET("images/search") fun get(
        @Query("limit") limit: Int = 1,
        @Header("x-api-key") authorizationKey: String
            = "8b9e0a37-e650-474f-8b22-d67287de4c61"//NOCOMMIT
//            = "SIGN UP FOR KEY HERE: https://thecatapi.com/"

    ): Call<List<CatData>>
}