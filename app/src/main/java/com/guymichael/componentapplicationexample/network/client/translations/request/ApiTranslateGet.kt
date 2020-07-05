package com.guymichael.componentapplicationexample.network.client.translations.request

import com.guymichael.componentapplicationexample.network.client.translations.response.ApiResponseTranslations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiTranslateGet {

    /**
     * @param sourceLang language code of the input. Two-letter ISO 639-1:2002,
     * or 'auto' to auto-detect
     * @param targetLang language code of the translation (output). Two-letter ISO 639-1:2002
     * @param input the text to translate
     */
    @GET("translation/text/translate") fun translate(
        @Query("input") input: String,
        @Query("target") targetLang: String = "en",
        @Query("source") sourceLang: String = "auto",

        @Header("x-rapidapi-host") host: String
            = "systran-systran-platform-for-language-processing-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") authorizationKey: String
            = "ee28e162acmsh052e71311b58672p1ce706jsnc9a7eeea6d6b"//NOCOMMIT
//            = "SIGN UP FOR KEY HERE: https://rapidapi.com/systran/api/systran-io-translation-and-nlp"

    ): Call<ApiResponseTranslations>
}