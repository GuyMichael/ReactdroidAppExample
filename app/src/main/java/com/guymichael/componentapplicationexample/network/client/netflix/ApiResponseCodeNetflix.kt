package com.guymichael.componentapplicationexample.network.client.netflix

import androidx.annotation.IntDef

@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    ApiResponseCodeNetflix.OK,
    ApiResponseCodeNetflix.BAD_REQUEST,
    ApiResponseCodeNetflix.UNAUTHORIZED,
    ApiResponseCodeNetflix.NOT_FOUND,
    ApiResponseCodeNetflix.GENERAL
)
annotation class ApiResponseCodeNetflix {
    
    companion object {
        const val OK = 200

        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404

        const val GENERAL = 599
    }
}

fun parseApiResponseCodeNetflix(raw: Int?): Int {
    return when(raw) {
        ApiResponseCodeNetflix.OK,
        ApiResponseCodeNetflix.NOT_FOUND,
        ApiResponseCodeNetflix.UNAUTHORIZED,
        ApiResponseCodeNetflix.GENERAL,
        ApiResponseCodeNetflix.BAD_REQUEST
        -> raw

        else -> ApiResponseCodeNetflix.GENERAL
    }
}