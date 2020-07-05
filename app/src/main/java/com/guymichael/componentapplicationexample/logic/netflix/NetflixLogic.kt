package com.guymichael.componentapplicationexample.logic.netflix

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.network.ApiController
import com.guymichael.componentapplicationexample.network.client.netflix.ApiResponseCodeNetflix
import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixGenreData
import com.guymichael.componentapplicationexample.network.client.netflix.request.ApiNetflixGenreGet
import com.guymichael.componentapplicationexample.network.client.netflix.request.ApiNetflixTitlesGet
import com.guymichael.componentapplicationexample.network.client.netflix.response.ApiResponseNetflixGenres
import com.guymichael.componentapplicationexample.network.client.netflix.response.ApiResponseNetflixTitles
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.componentapplicationexample.network.of
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeNetflixGenre
import com.guymichael.componentapplicationexample.store.datatype.DataTypeNetflixTitle
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.Logger
import com.guymichael.reactiveapp.network.model.ApiError
import com.guymichael.reactiveapp.network.model.ApiRequest

object NetflixLogic {

    /**
     * [Loads][DataTypeNetflixGenre.getPersistedData] or [fetches][fetchGenres] (if not in state/db)
     * [NetflixGenreData]s into (dispatch) `MainStore`.
     *
     * @param state to check if the data is already in state, in which case this is a no-op
     */
    fun loadOrFetchGenres(state: GlobalState = MainStore.state): APromise<Unit> {
        return ApiController.loadOrFetch(DataTypeNetflixGenre
            , { fetchGenres().thenMap(::mapGenresResponseToData) }
            , state
        )

        .catch { e ->
            ApiError.parseMany(e).forEach { when(it.code) {
                ApiResponseCodeNetflix.UNAUTHORIZED -> Logger.e(
                    NetflixLogic::class, "fetchAndDispatchGenres() failed: " +
                            "unauthorized.\nReplace ApiCountriesGet's 'authorizationKey' argument with" +
                            "a key from here (Sign Up required for free) :\n" +
                            "https://rapidapi.com/unogs/api/unogs"
                )

                else-> {}
            }}
        }
    }

    fun fetchAndDispatchTitles(): APromise<ApiResponseNetflixTitles> {
        return ApiRequest.of(ApiNetflixTitlesGet::class, ApiClientName.NETFLIX
            , { it.advancedSearch() }
            , DataTypeNetflixTitle
            , { it.ITEMS } //append titles
            , merge = true
        )

        .catch { e ->
            ApiError.parseMany(e).forEach { when(it.code) {
                ApiResponseCodeNetflix.UNAUTHORIZED -> Logger.e(
                    NetflixLogic::class, "fetchAndDispatchTitles() failed: " +
                            "unauthorized.\nReplace ApiCountriesGet's 'authorizationKey' argument with" +
                            "a key from here (Sign Up required for free) :\n" +
                            "https://rapidapi.com/unogs/api/unogs"
                )

                else-> {}
            }}
        }
    }






    private fun fetchGenres(): APromise<ApiResponseNetflixGenres> {
        return ApiRequest.of(ApiNetflixGenreGet::class, ApiClientName.NETFLIX) { it.getAll() }
    }

    private fun mapGenresResponseToData(response: ApiResponseNetflixGenres): List<NetflixGenreData> {
        return response.ITEMS.map { pairMap ->  pairMap.entries.first().let { (name, titleIds) ->
            NetflixGenreData(
                name,
                titleIds.map { it.toLong() }
            )
        }}
    }
}