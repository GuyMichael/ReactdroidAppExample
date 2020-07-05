package com.guymichael.componentapplicationexample.network.client.netflix.request

import com.guymichael.componentapplicationexample.network.client.netflix.response.ApiResponseNetflixTitles
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNetflixTitlesGet {

    @GET("aaapi.cgi") fun advancedSearch(
        @Query("q") q: String = "get:new7-!1900,2018-!0,5-!0,10-!0-!Any-!Any-!Any-!gt100-!No",
        @Query("t") t: String = "ns",
        @Query("cl") cl: String = "all",
        @Query("st") st: String = "adv",
        @Query("ob") ob: String = "Relevance",
        @Query("p") p: String = "1",
        @Query("sa") sa: String = "and",

        @Header("x-rapidapi-host") host: String
            = "unogs-unogs-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") authorizationKey: String
            = "ee28e162acmsh052e71311b58672p1ce706jsnc9a7eeea6d6b"//NOCOMMIT
//            = "SIGN UP FOR KEY HERE: https://rapidapi.com/systran/api/systran-io-translation-and-nlp"

    ): Call<ApiResponseNetflixTitles>
}