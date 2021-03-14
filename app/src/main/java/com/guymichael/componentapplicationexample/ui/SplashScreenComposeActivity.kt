package com.guymichael.componentapplicationexample.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.dispatchTyped
import com.guymichael.componentapplicationexample.store.reducers.WelcomeDialogShown
import com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core.connect
import com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core.observeAsState
import com.guymichael.kotlinreact.Logger
import com.guymichael.reactdroid.extensions.components.text.TextProps

class SplashScreenComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Row {
                Column {
                    ConnectedSplashScreen() //connect(Composable)
                    SplashScreenCompose2()  //Store.observeAsState
                }

                //if this one is enabled - the whole Row will re-render!
                /*Column {
                    val props by MainStore.observeAsState { TextProps(WelcomeDialogShown.get(it).toString()) }
                    SplashScreenCompose(props)
                }*/

                //this one should not re-render...
                Column {
                    SplashScreenCompose(TextProps(WelcomeDialogShown.get(MainStore.state).toString()))
                }
            }

        }

        APromise.delay(5000) {
            MainStore.dispatchTyped(WelcomeDialogShown, false)
        }

        APromise.delay(10000) {
            MainStore.dispatchTyped(WelcomeDialogShown, true)
        }
    }
}

@Composable
private fun SplashScreenCompose(props: TextProps) {
    Logger.d("ComposeConnect", "SplashScreenCompose() called") //NOCOMMIT log
    Text(props.text?.toString() ?: "", color = Color.Black)
}
@Composable
private fun SplashScreenCompose2() {
    val props by MainStore.observeAsState { TextProps(WelcomeDialogShown.get(it).toString()) }
    Text(props.text?.toString() ?: "", color = Color.Black)
}
private val ConnectedSplashScreen = connect(
    { SplashScreenCompose(it) }
    , { state ->
        TextProps(WelcomeDialogShown.get(state).toString())
    }
    , { MainStore }
)