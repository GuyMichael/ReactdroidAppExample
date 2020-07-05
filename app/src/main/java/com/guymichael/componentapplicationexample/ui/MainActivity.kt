package com.guymichael.componentapplicationexample.ui

import android.content.Intent
import android.view.ViewGroup
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.google.android.material.snackbar.Snackbar
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.navigation.CLIENT_PAGE
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.activities.BaseActivity
import com.guymichael.reactiveapp.utils.setStatusBarColor
import com.guymichael.reactdroid.core.model.AComponent

class MainActivity : BaseActivity<EmptyOwnProps, AComponent<EmptyOwnProps, *, *>, EmptyOwnProps>()
        , CameraXConfig.Provider {

    override val clientPage = CLIENT_PAGE.MAIN
    override fun getLayoutRes() = R.layout.activity_main
    override fun getMenuRes() = R.menu.main
    override fun mapIntentToProps(intent: Intent) = EmptyOwnProps
    override fun createPageComponent(activityView: ViewGroup) = MainPage.connected(activityView)
    override fun mapActivityPropsToPageProps(props: EmptyOwnProps) = props

    override fun onPrepareActivityFrame() {
        setStatusBarColor(R.color.dayNightPrimary)

        withToolbar(R.id.toolbar)

        withFab(R.id.fab) { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        withDrawer(
            R.id.drawer_layout
            , R.id.nav_view
            , R.id.nav_host_fragment

            //drawer menu
            //for each supplied menu id, its relevant page will have the drawer ('burger') icon, instead of 'back'.
            // so you can say that these items define the 'main' pages of the app, UX-wise
            , R.id.nav_home
            , R.id.nav_cats
            , R.id.nav_netflix
            , R.id.nav_countries
            , R.id.nav_qr
//            , R.id.nav_input_sheet      left out to show 'back' on toolbar
//            , R.id.nav_frag_pager
//            , nav_recycler_pager
            , R.id.nav_camera
            , R.id.nav_translation
        )

        //enable to test deep link with parameter
        /*APromise.delay(1500) {
            //open "https://app.componentappexample.com/netflix/genre?id=15"
            DeepLinkLogic.openDeepLink(this, Utils.encodeUrl(
                "${getString(R.string.deepLinkBaseUrl)}/${CLIENT_PAGE.NETFLIX_GENRE.path}"
                , "id" to 15
            )).execute()
        }*/
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}