package com.guymichael.componentapplicationexample.ui

import android.content.Intent
import android.view.ViewGroup
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.navigation.CLIENT_PAGE
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.activities.BaseActivity
import com.guymichael.reactdroid.extensions.navigation.NavigationLogic
import com.guymichael.reactdroid.extensions.router.openDeepLinkOrReject

class SplashScreen : BaseActivity<EmptyOwnProps, EmptyPage, EmptyOwnProps>() {
    override val clientPage: Nothing? = null
    override fun getLayoutRes() = R.layout.activity_splashscreen
    override fun getMenuRes(): Nothing? = null
    override fun mapIntentToProps(intent: Intent) = EmptyOwnProps
    override fun createPageComponent(activityView: ViewGroup) = EmptyPage(activityView)
    override fun mapActivityPropsToPageProps(props: EmptyOwnProps) = props

    override fun onPrepareActivityFrame() {}

    override fun onIntentChanged(newIntent: Intent) {
        openDeepLinkOrReject(newIntent).then {
            //deep link opened

        }.catch {
            //no deep link, open main page (even if it came from onNewIntent()
            // for some reason which shouldn't exist as the splash screen closes immediately
            // by manifest's noHistory="true"
            APromise.delayWhileAlive(this, 1000) {
                NavigationLogic.open(CLIENT_PAGE.MAIN, this, EmptyOwnProps).execute()
            }



        }.execute()
    }
}