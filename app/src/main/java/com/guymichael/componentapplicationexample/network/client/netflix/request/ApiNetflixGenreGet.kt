package com.guymichael.componentapplicationexample.network.client.netflix.request

import com.guymichael.componentapplicationexample.network.client.netflix.response.ApiResponseNetflixGenres
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiNetflixGenreGet {

    @GET("api.cgi") fun getAll(
        @Query("t") t: String = "genres",

        @Header("x-rapidapi-host") host: String = "unogs-unogs-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") authorizationKey: String
            = "ee28e162acmsh052e71311b58672p1ce706jsnc9a7eeea6d6b"//NOCOMMIT
//            = "SIGN UP FOR KEY HERE: https://rapidapi.com/systran/api/systran-io-translation-and-nlp"

    ): Call<ApiResponseNetflixGenres>
}