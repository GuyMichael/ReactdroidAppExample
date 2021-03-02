package com.guymichael.componentapplicationexample.ui

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.reducers.GeneralReducerKey
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.getString
import com.guymichael.reactdroid.core.model.AHOC
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactdroid.extensions.components.dialog.AlertDialogProps
import com.guymichael.reactdroid.extensions.components.dialog.withAlertDialog
import com.guymichael.reactdroidflux.model.connect

class MainPage(v: View) : ASimpleComponent<MainPageProps>(v) {

    //this dialog can even be a member of the MyApplication which will wait for the first Activity that opens
    // (whatever it is) and show a dialog on it. Good for dialogs which should be shown no matter what
    // (e.g. loggedIn or not, got from a deepLink to non-main page, etc.)
    private val cDialog = withAlertDialog(
        onDismiss = { MainStore.dispatch(GeneralReducerKey.welcomeDialogShown, false) }
    )

    override fun render() {
        cDialog.onRender(AlertDialogProps(
            props.welcomeDialogShown
            , title = getString(R.string.home_dialog_title)
            , message = getString(R.string.home_dialog_msg)
            , okBtn = getString(R.string.dialog_btn_ok) to { _ -> null}
            , cancelBtn = null
        ))
    }



    companion object {
        fun connected(v: View): AHOC<EmptyOwnProps, *, *, *, EmptyOwnState> {
            return connect(
                MainPage(v)
                , MainPageProps.Companion::mapStateToProps
                , { MainStore }
            )
        }
    }
}