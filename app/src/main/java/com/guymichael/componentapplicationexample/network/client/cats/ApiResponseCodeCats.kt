package com.guymichael.componentapplicationexample.network.client.cats

import androidx.annotation.IntDef

@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    ApiResponseCodeCats.OK,
    ApiResponseCodeCats.BAD_REQUEST,
    ApiResponseCodeCats.UNAUTHORIZED,
    ApiResponseCodeCats.NOT_FOUND,
    ApiResponseCodeCats.GENERAL
)
annotation class ApiResponseCodeCats {
    
    companion object {
        const val OK = 200

        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404

        const val GENERAL = 599
    }
}

fun parseApiResponseCodeCats(raw: Int?): Int {
    return when(raw) {
        ApiResponseCodeCats.OK,
        ApiResponseCodeCats.NOT_FOUND,
        ApiResponseCodeCats.UNAUTHORIZED,
        ApiResponseCodeCats.GENERAL,
        ApiResponseCodeCats.BAD_REQUEST
        -> raw

        else -> ApiResponseCodeCats.GENERAL
    }
}