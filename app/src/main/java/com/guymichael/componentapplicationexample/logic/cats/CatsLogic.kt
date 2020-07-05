package com.guymichael.componentapplicationexample.logic.cats

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.network.client.cats.data.CatData
import com.guymichael.componentapplicationexample.network.client.cats.request.ApiCatsGet
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.componentapplicationexample.network.of
import com.guymichael.componentapplicationexample.store.datatype.DataTypeCats
import com.guymichael.reactiveapp.network.model.ApiRequest

object CatsLogic {

    fun fetchAndDispatchNextCats(limit: Int): APromise<List<CatData>> {
        //this api just returns more (new) cats at each call (probably session on server)
        // so no need for pagination logic here
        return ApiRequest.of(ApiCatsGet::class, ApiClientName.CATS, { it.get(limit) }
            , DataTypeCats
            , { it }
            , merge = true //add more cats to the cache as they come!
        )
    }
}