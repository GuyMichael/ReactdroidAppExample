package com.guymichael.componentapplicationexample.network

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.network.model.ApiClientName
import com.guymichael.kotlinflux.extensions.data.model.StoreDataAPIController
import com.guymichael.kotlinflux.extensions.data.model.StoreDataType
import com.guymichael.reactiveapp.network.model.ApiRequest
import retrofit2.Call
import kotlin.reflect.KClass

object ApiController : StoreDataAPIController()




//add (extend) helper method to ApiRequest
/**
 * Convenience method for declaring 'fetch' and 'dispatch' logic in the same call.
 *
 * @param service a Retrofit interface (annotated) which represents an API
 * @param callSupplier given the `service`, call it to create the Retrofit `Call`
 *
 * @see StoreDataAPIController.prepare for docs
 * */
inline fun <SERVICE : Any, reified API_RESPONSE : Any, DATA : Any, TYPE: StoreDataType<DATA>>
ApiRequest.of(
    //basic ApiRequest.of()
    service: KClass<SERVICE>
    , apiClientName: @ApiClientName String
    , crossinline callSupplier: (SERVICE) -> Call<API_RESPONSE>

    //store/data connection
    , dataType: TYPE
    , noinline mapResponseToData: (API_RESPONSE) -> List<DATA>
    , merge: Boolean

    //optionals
    , noinline persistSideEffects: (API_RESPONSE) -> Unit = {}
    , noinline dispatchSideEffects: (API_RESPONSE) -> Unit = {}
    , logErrors: Boolean = true
    , persist: Boolean = true
): APromise<API_RESPONSE> {

    return ApiController.prepare(
        of(service, apiClientName, callSupplier)
        , dataType
        , { res, _ -> dataType.dispatchLoaded(mapResponseToData(res), merge, persist) }
        , persistSideEffects
        , dispatchSideEffects
        , logErrors
    )
}