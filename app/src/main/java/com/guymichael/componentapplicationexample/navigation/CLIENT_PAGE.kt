package com.guymichael.componentapplicationexample.navigation

import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.withBlockUiProgress
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.OwnProps
import com.guymichael.reactdroid.core.activity.ComponentActivity
import com.guymichael.reactdroid.extensions.navigation.ActivityLogic
import com.guymichael.reactdroid.extensions.navigation.ClientPageIntf
import com.guymichael.reactdroid.extensions.navigation.model.NavigationAction
import com.guymichael.reactiveapp.activities.BaseActivity
import kotlin.reflect.KClass

/**
 * An enum containing ALL the pages in the app which  implementing the [ClientPageIntf] interface.
 */
enum class CLIENT_PAGE(
        override val path: String,
        override val openPage: (ComponentActivity<*>, NavigationAction<*>)
                -> APromise<out ComponentActivity<*>>,
        override val allowToOpen: () -> Boolean = ::allowToOpenIfLoggedIn,
        override val mapExtrasToPropsOrNull: (Map<String, String>) -> OwnProps?
    ) : ClientPageIntf {

    MAIN(""
        , openPage = { context, action ->
            openActivity(context
                , com.guymichael.componentapplicationexample.ui.MainActivity::class
                , action
            )
        }
        , mapExtrasToPropsOrNull = { EmptyOwnProps }
    ),

    HOME("home"
        , openPage = { context, action ->
            MAIN.openPage(context, action.cloneWith(EmptyOwnProps))
                .then {(it as BaseActivity<*, *, *>) //mainActivity ->
                    //this method throws, in which case the wrapping promise will be rejected as expected
                    .openDrawerDeepLink(
                        com.guymichael.componentapplicationexample.R.id.nav_home
                        , EmptyOwnProps
                    )
                }
        }
        , mapExtrasToPropsOrNull = { EmptyOwnProps }
    ),

    CATS_CATALOG("cats"
        , openPage = { context, action ->
            //open (make sure) MainActivity, then navigate to the fragment inside
            MAIN.openPage(context, action.cloneWith(EmptyOwnProps))
                .then {(it as BaseActivity<*, *, *>) //mainActivity ->
                    //this method throws, in which case the wrapping promise will be rejected as expected
                    .openDrawerDeepLink(
                        com.guymichael.componentapplicationexample.R.id.nav_cats
                        , EmptyOwnProps
                    )
                }
        }
        , mapExtrasToPropsOrNull = { EmptyOwnProps }
    ),

    //an example of a DeepLink directly to a Fragment
    NETFLIX_CATALOG("netflix"
        , openPage = { context, action ->
            //open (make sure) MainActivity, then navigate to the fragment inside
            MAIN.openPage(context, action.cloneWith(EmptyOwnProps))
                .then {(it as BaseActivity<*, *, *>) //mainActivity ->
                    //this method throws, in which case the wrapping promise will be rejected as expected
                    .openDrawerDeepLink(
                        com.guymichael.componentapplicationexample.R.id.nav_netflix
                        , com.guymichael.kotlinreact.model.props.LongProps(null)
                    )
                }
        }
        , mapExtrasToPropsOrNull = { EmptyOwnProps }
    ),

    //an example of a DeepLink directly to some state inside a Fragment
    NETFLIX_GENRE("netflix/genre"
        , openPage = { context, action  ->
            //open (make sure) MainActivity, then navigate to the fragment inside
            NETFLIX_CATALOG.openPage(context, action.cloneWith(EmptyOwnProps))
                .then {(it as BaseActivity<*, *, *>) //mainActivity ->
                    //this method throws, in which case the wrapping promise will be rejected as expected
                    .openDrawerDeepLink(
                        com.guymichael.componentapplicationexample.R.id.nav_netflix
                        , action.props
                    )
                }
        }
        , mapExtrasToPropsOrNull = {
            com.guymichael.kotlinreact.model.props.LongProps(it["id"]?.toLongOrNull())
        }
    ),

    CAMERA("camera"
        , openPage = { context, action ->
            //open (make sure) MainActivity, then navigate to the fragment inside
            MAIN.openPage(context, action.cloneWith(EmptyOwnProps))
                .thenMapWithContextOrCancel(context) { _, mainActivity ->
                    //this method throws, in which case the wrapping promise will be rejected as expected
                    (mainActivity as BaseActivity<*, *, *>).openDrawerDeepLink(
                        com.guymichael.componentapplicationexample.R.id.nav_camera
                        , action.props
                    )

                    mainActivity
                }
        }
        , mapExtrasToPropsOrNull = { EmptyOwnProps }
    )

    ;

    override fun getPageName() = this.name

    companion object {
        @JvmStatic
        fun parse(key: String): CLIENT_PAGE? {
            return key.let {
                values().firstOrNull { it.path == key }
            }
        }
    }
}





private fun allowToOpenIfLoggedIn(): Boolean {
    return true//UserLogic.isLoggedIn()
}

private fun <P : OwnProps, T : ComponentActivity<P>>
openActivity(context: ComponentActivity<*>, cls: KClass<T>, action: NavigationAction<*>): APromise<T> {
    @Suppress("UNCHECKED_CAST")
    return ActivityLogic.openActivity(
        context
        , cls.java
        , action.props as P
        , action.inOutAnimations
        , action.transitions
        , action.forResult_requestCode
    )

    //showLoader(?)
    .let { if (action.showLoader && context is BaseActivity<*, *, *>) {
        it.withBlockUiProgress(context)
    } else {
        it
    }}
}

private fun <P : OwnProps> NavigationAction<*>.cloneWith(newProps: P): NavigationAction<P> {
    return NavigationAction(
        newProps
        , inOutAnimations, transitions, forResult_requestCode, showLoader, intentFlags
    )
}