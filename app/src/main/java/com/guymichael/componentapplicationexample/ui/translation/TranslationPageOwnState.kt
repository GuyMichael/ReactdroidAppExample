package com.guymichael.componentapplicationexample.ui.translation

import com.guymichael.kotlinreact.model.OwnState

data class TranslationPageOwnState(
    val input: String?,
    val resultId: String?
) : OwnState() {

    override fun getAllMembers() = listOf(
        input, resultId
    )


    companion object {
        fun empty() = TranslationPageOwnState(null, null)
    }
}