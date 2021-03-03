package com.guymichael.componentapplicationexample

import android.app.Application
import com.guymichael.componentapplicationexample.network.client.cats.parseApiResponseCodeCats
import com.guymichael.componentapplicationexample.network.client.countries.parseApiResponseCodeCountries
import com.guymichael.componentapplicationexample.network.client.netflix.parseApiResponseCodeNetflix
import com.guymichael.componentapplicationexample.network.client.translations.parseApiResponseCodeTranslations
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.kotlinreact.Logger
import com.guymichael.lib.persist.db.MyObjectBox
import com.guymichael.reactiveapp.network.model.ApiClient
import com.guymichael.reactiveapp.persist.db.DbLogic
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApplicationStartupLogic {

    fun createApiClients(defaultJsonParser: MoshiConverterFactory): List<ApiClient> {
        return listOf(
            createCatsApiClient(defaultJsonParser)
            , createNetflixApiClient(defaultJsonParser)
            , createCountriesApiClient(defaultJsonParser)
            , createTranslationsApiClient(defaultJsonParser)
        )
    }

    @Throws(LinkageError::class)
    fun createBoxStore(app: Application): BoxStore {
        return try {
            MyObjectBox.builder()
                .androidContext(app.applicationContext)
                .build()
        } catch (e: LinkageError) {
            Logger.e(DbLogic::class, "Objectbox init failed: ${e.message}")
            throw e
        }
    }




    private fun createCatsApiClient(jsonParser: MoshiConverterFactory): ApiClient {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY

                //redact headers to prevent leaking sensitive data
                it.redactHeader("x-api-key")
            }
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(client)
            .addConverterFactory(jsonParser)
            .build()

        return ApiClient(retrofit, ApiClientName.CATS, ::parseApiResponseCodeCats)
    }

    private fun createNetflixApiClient(jsonParser: MoshiConverterFactory): ApiClient {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY

                //redact headers to prevent leaking sensitive data
                it.redactHeader("x-rapidapi-key")
            }
        ).build()

        val converterFactory = jsonParser
            .asLenient()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://unogs-unogs-v1.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(converterFactory)
            .build()

        return ApiClient(retrofit, ApiClientName.NETFLIX, ::parseApiResponseCodeNetflix)
    }

    private fun createCountriesApiClient(JsonParser: Converter.Factory): ApiClient {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY

                //redact headers to prevent leaking sensitive data
                it.redactHeader("x-rapidapi-key")
            }
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries-v1.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(JsonParser)
            .build()

        return ApiClient(retrofit, ApiClientName.COUNTRIES, ::parseApiResponseCodeCountries)
    }

    private fun createTranslationsApiClient(jsobParser: MoshiConverterFactory): ApiClient {
        val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY

                //redact headers to prevent leaking sensitive data
                it.redactHeader("x-rapidapi-key")
            }
        ).build()

        val converterFactory = jsobParser
            .asLenient()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://systran-systran-platform-for-language-processing-v1.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(converterFactory)
            .build()

        return ApiClient(retrofit, ApiClientName.TRANSLATIONS, ::parseApiResponseCodeTranslations)
    }
}