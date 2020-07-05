package com.guymichael.componentapplicationexample.ui.home

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AHOC
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactdroid.extensions.animation.renderTextOrGone
import com.guymichael.reactdroid.extensions.components.text.withText
import com.guymichael.reactdroidflux.model.connect

class HomePage(v: View) : ASimpleComponent<HomeProps>(v) {
    private val cTxt = withText(R.id.text_home)

    override fun render() {
        //render text resource with fadeIn animation
        cTxt.renderTextOrGone(R.string.home_hello_world.takeIf { props.mainTxtShown }
            , animateVisibility = true  //view is invisible in xml, so animation works on page-view.
            // It is most usable when views visibility is dynamic
            // according to some state (so they easily hide and show
            // with fade animation)
            , animDuration = 1750
            , animStartAlpha = 0f
        )
    }



    companion object {
        /** Connects the [HomePage] to the [MainStore], showing the main text
         * only when the welcome dialog is hidden */
        fun connected(v: View): AHOC<EmptyOwnProps, *, *, *, EmptyOwnState> {
            return connect(
                HomePage(v)
                , HomeProps.Companion::mapStateToProps
                , { MainStore }
            )
        }
    }
}