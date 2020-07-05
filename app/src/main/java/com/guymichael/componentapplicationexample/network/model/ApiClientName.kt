package com.guymichael.componentapplicationexample.network.model

import androidx.annotation.StringDef

@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    ApiClientName.COUNTRIES,
    ApiClientName.TRANSLATIONS,
    ApiClientName.NETFLIX,
    ApiClientName.CATS
)
annotation class ApiClientName {
    companion object {
        const val COUNTRIES = "countryInfo"
        const val TRANSLATIONS = "translator"
        const val NETFLIX = "netflix"
        const val CATS = "cats"
    }
}