package com.guymichael.componentapplicationexample.ui.netflix

import com.guymichael.kotlinreact.model.OwnState

data class NetflixCatalogOwnState(
        val genreId: Long?
    ) : OwnState() {

    override fun getAllMembers() = listOf(
        genreId
    )
}