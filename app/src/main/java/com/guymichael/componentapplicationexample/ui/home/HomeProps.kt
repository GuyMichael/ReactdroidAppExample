package com.guymichael.componentapplicationexample.ui.home

import com.guymichael.componentapplicationexample.store.reducers.WelcomeDialogShown
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.OwnProps

data class HomeProps(val mainTxtShown: Boolean) : OwnProps() {
    override fun getAllMembers() = listOf(mainTxtShown)

    companion object {
        fun mapStateToProps(state: GlobalState, apiProps: EmptyOwnProps): HomeProps {
            return HomeProps(
                mainTxtShown = !WelcomeDialogShown.get(state)
            )
        }
    }
}