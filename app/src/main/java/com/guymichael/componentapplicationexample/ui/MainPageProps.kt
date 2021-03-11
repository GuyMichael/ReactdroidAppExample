package com.guymichael.componentapplicationexample.ui

import com.guymichael.componentapplicationexample.store.reducers.WelcomeDialogShown
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.OwnProps

data class MainPageProps(val welcomeDialogShown: Boolean) : OwnProps() {
    override fun getAllMembers() = listOf(welcomeDialogShown)

    companion object {
        fun mapStateToProps(state: GlobalState, apiProps: EmptyOwnProps): MainPageProps {
            return MainPageProps(
                welcomeDialogShown = WelcomeDialogShown.get(state)
            )
        }
    }
}