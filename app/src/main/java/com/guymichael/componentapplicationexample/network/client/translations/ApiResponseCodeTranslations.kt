package com.guymichael.componentapplicationexample.network.client.translations

import androidx.annotation.IntDef

@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    ApiResponseCodeTranslations.OK,
    ApiResponseCodeTranslations.BAD_REQUEST,
    ApiResponseCodeTranslations.UNAUTHORIZED,
    ApiResponseCodeTranslations.NOT_FOUND,
    ApiResponseCodeTranslations.GENERAL
)
annotation class ApiResponseCodeTranslations {
    
    companion object {
        const val OK = 200

        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404

        const val GENERAL = 599
    }
}

fun parseApiResponseCodeTranslations(raw: Int?): Int {
    return when(raw) {
        ApiResponseCodeTranslations.OK,
        ApiResponseCodeTranslations.NOT_FOUND,
        ApiResponseCodeTranslations.UNAUTHORIZED,
        ApiResponseCodeTranslations.GENERAL,
        ApiResponseCodeTranslations.BAD_REQUEST
        -> raw

        else -> ApiResponseCodeTranslations.GENERAL
    }
}