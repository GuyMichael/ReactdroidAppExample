package com.guymichael.componentapplicationexample.network.client.countries

import androidx.annotation.IntDef

@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    ApiResponseCodeCountries.OK,
    ApiResponseCodeCountries.BAD_REQUEST,
    ApiResponseCodeCountries.UNAUTHORIZED,
    ApiResponseCodeCountries.NOT_FOUND,
    ApiResponseCodeCountries.GENERAL
)
annotation class ApiResponseCodeCountries {
    
    companion object {
        const val OK = 200

        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404

        const val GENERAL = 599
    }
}

fun parseApiResponseCodeCountries(raw: Int?): Int {
    return when(raw) {
        ApiResponseCodeCountries.OK,
        ApiResponseCodeCountries.NOT_FOUND,
        ApiResponseCodeCountries.UNAUTHORIZED,
        ApiResponseCodeCountries.GENERAL,
        ApiResponseCodeCountries.BAD_REQUEST
        -> raw

        else -> ApiResponseCodeCountries.GENERAL
    }
}