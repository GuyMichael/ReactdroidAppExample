package com.guymichael.componentapplicationexample.ui.countries

import com.guymichael.kotlinreact.model.OwnState

data class CountriesPageState(val constraint: String? = null) : OwnState() {
    override fun getAllMembers() = listOf(constraint)
}