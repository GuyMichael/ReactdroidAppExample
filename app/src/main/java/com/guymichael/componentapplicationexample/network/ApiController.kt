package com.guymichael.componentapplicationexample.network

import com.guymichael.kotlinflux.extensions.data.model.StoreDataAPIController

object ApiController : StoreDataAPIController()


//an example of a private API (with auth access) usage
/*inline fun <S : Any, reified T : Any> ApiRequest.ofPrivate(
    service: KClass<S>
    , errorResponseType: KClass<*>? = MyAppApiErrorResponse::class
    , state: GlobalState = MainStore.state
    , crossinline callSupplier: (PrivateApiAuth, S) -> Call<T>
): APromise<T> {

    return UserLogic.privateApiPromiseOrReject(state)
        .thenAwait { auth ->
            ApiRequest.of(service, errorResponseType) {
                callSupplier.invoke(auth, it)
            }
        }
}
fun UserLogic.privateApiPromiseOrReject(state: GlobalState = MainStore.state): APromise<PrivateApiAuth> {
    return  getCurrentUserId(state)?.let { userId ->
        //e.g. from SharedPref
        getAuthAccessTokenAndType()?.let { accessTokenAndType ->
                        //just some model
            APromise.of(PrivateApiAuth(userId, accessTokenAndType.first, accessTokenAndType.second))
        }}

        ?: APromise.ofReject("Private API: No logged in user, or no access token found")
}*/