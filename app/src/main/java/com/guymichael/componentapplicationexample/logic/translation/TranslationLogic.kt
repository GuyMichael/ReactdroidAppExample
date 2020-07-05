package com.guymichael.componentapplicationexample.logic.translation

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.network.client.translations.ApiResponseCodeTranslations
import com.guymichael.componentapplicationexample.network.client.translations.request.ApiTranslateGet
import com.guymichael.componentapplicationexample.network.client.translations.response.ApiResponseTranslations
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.componentapplicationexample.network.of
import com.guymichael.componentapplicationexample.store.datatype.DataTypeTranslation
import com.guymichael.kotlinreact.Logger
import com.guymichael.reactiveapp.network.model.ApiError
import com.guymichael.reactiveapp.network.model.ApiRequest

object TranslationLogic {

    /**
     * We use `fetchAndDispatch` when naming, so to be clear that the response is handled
     * in means of Store, persist and catching exceptions. In most scenarios, you should not need
     * to use the returned promise at all, as by connecting to a `Store`, a `Component` will
     * automatically re-render for you
     */
    fun fetchAndDispatchTranslations(input: String
            , targetLang: String = "pt", sourceLang: String = "auto"
        ): APromise<ApiResponseTranslations> {

        return ApiRequest.of(
            ApiTranslateGet::class     //select retrofit service (interface)
            , ApiClientName.TRANSLATIONS
            , { it.translate(input, targetLang, sourceLang) }       //execute the api (interface method)
            , DataTypeTranslation       //define how to dispatch response data to a Store (DataReducer)
                                        // and persist (e.g. SharedPrefs, SQL)

            , { it.outputs } //extract relevant data to dispatch
            , merge = true                                            //append to current translations
        )

        .catch { e ->
            ApiError.parseMany(e).forEach { when(it.code) {
                ApiResponseCodeTranslations.UNAUTHORIZED -> Logger.e(
                    TranslationLogic::class, "fetchAndDispatchTranslations() failed: " +
                            "unauthorized.\nReplace ApiTranslateGet's 'authorizationKey' argument with" +
                            "a key from here (Sign Up required for free) :\n" +
                            "https://rapidapi.com/systran/api/systran-io-translation-and-nlp"
                )

                else-> {}
            }}
        }
    }
}