package com.guymichael.componentapplicationexample.logic.countries

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.network.ApiController
import com.guymichael.componentapplicationexample.network.client.countries.ApiResponseCodeCountries
import com.guymichael.componentapplicationexample.network.client.countries.data.CountryData
import com.guymichael.componentapplicationexample.network.client.countries.request.ApiCountriesGet
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeCountry
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.Logger
import com.guymichael.reactiveapp.network.model.ApiError
import com.guymichael.reactiveapp.network.model.ApiRequest

object CountriesLogic {

    fun loadOrFetchCountries(state: GlobalState = MainStore.state): APromise<Unit> {
        return ApiController.loadOrFetch(DataTypeCountry
            , { fetchCountries() }
            , state
        )

        .catch { e ->
            ApiError.parseMany(e).forEach { when(it.code) {
                ApiResponseCodeCountries.UNAUTHORIZED -> Logger.e(
                    CountriesLogic::class, "fetchAndDispatchCountries() failed: " +
                            "unauthorized.\nReplace ApiCountriesGet's 'authorizationKey' argument with" +
                            "a key from here (Sign Up required for free) :\n" +
                            "https://rapidapi.com/apilayernet/api/rest-countries-v1"
                )

                else-> {}
            }}
        }
    }




    private fun fetchCountries(): APromise<List<CountryData>> {
        return ApiRequest.of(           //wrap a http Call with an APromise
            ApiCountriesGet::class      //retrofit Call interface
            , ApiClientName.COUNTRIES   //base url
        ) { it.getAll() }               //execute the http Call
    }
}