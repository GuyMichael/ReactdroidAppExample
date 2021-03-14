package com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core

import androidx.annotation.MainThread
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinflux.model.Store
import com.guymichael.kotlinflux.model.StoreObserver
import com.guymichael.kotlinreact.Logger
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.OwnProps
import com.guymichael.reactdroid.core.Utils
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Connect [Composable]s to a [Store] with no `apiProps`
 */
fun <OWN_PROPS : OwnProps> connect(
    component: @Composable (ownProps: OWN_PROPS) -> Unit
    , mapStateToProps: (state: GlobalState) -> OWN_PROPS
    , storeSupplier: () -> Store
) : @Composable () -> Unit {
    val connected = connect(
        component
        , { state, _: EmptyOwnProps -> mapStateToProps(state)}
        , storeSupplier
    )

    return { connected(EmptyOwnProps) }
}

/**
 * Connect [Composable]s to a [Store] to form a new [Composable] that receives `apiProps`
 * instead of `ownProps`
 */
fun <API_PROPS : OwnProps, OWN_PROPS : OwnProps> connect(
    component: @Composable (ownProps: OWN_PROPS) -> Unit
    , mapStateToProps: (state: GlobalState, apiProps: API_PROPS) -> OWN_PROPS
    , storeSupplier: () -> Store
) : @Composable (API_PROPS) -> Unit {

    // wrap given component with a connected-to-Store Composable.
    // that Composable listens on State<OWN_PROPS> that may update when the global state updates -
    // according to mapStateTo(own)Props results' equality
    return { apiProps ->
        Logger.d("ComposeConnect", "connect 'onRender' was called. apiProps: $apiProps") //NOCOMMIT log

        //execute the inner composable
        component.invoke(
            //ownProps
            storeSupplier().observeAsState(apiProps, mapStateToProps).value
        )
    }
}

/**
 * Subscribe to a [Store] and observe changes as a [State] of [OwnProps],
 * with no `apiProps`
 */
@Composable
fun <P : OwnProps> Store.observeAsState(
    mapStateToProps: (state: GlobalState) -> P
): State<P> {
    return this
        .observeAsState(EmptyOwnProps, mapStateToProps = { state, _ -> mapStateToProps(state) })
}

/**
 * Subscribe to a [Store] and observe changes as a [State] of [OwnProps].
 * @param apiProps a custom input to merge with the [GlobalState] on each update
 */
@Composable
fun <API_PROPS : OwnProps, OWN_PROPS : OwnProps> Store.observeAsState(
    apiProps: API_PROPS
    , mapStateToProps: (state: GlobalState, apiProps: API_PROPS) -> OWN_PROPS
): State<OWN_PROPS> {
    return mapStateToProps(this.state, apiProps)               //ownProps from state+props
        .connectAsState(apiProps, mapStateToProps, { this })   //State<OwnProps> - Store connected
}

/**
 * Starts observing this [OwnProps] as a subscriber to the given [Store]
 * and represents its values via [State].
 * Every time that a change to the [GlobalState] also affects this [OwnProps],
 * the returned [State] will be updated causing recomposition of every [State.value] usage.
 * The inner observer will automatically be removed when this composable disposes or
 * the current [LifecycleOwner] moves to the [Lifecycle.State.DESTROYED] state.
 */
@Composable
fun <API_PROPS : OwnProps, OWN_PROPS : OwnProps> OWN_PROPS.connectAsState(
    apiProps: API_PROPS
    , mapStateToProps: (state: GlobalState, apiProps: API_PROPS) -> OWN_PROPS
    , storeSupplier: () -> Store
): State<OWN_PROPS> {

    val lifecycleOwner = LocalLifecycleOwner.current
    val state = remember { mutableStateOf(this) } //'this' as 'initial' value
    val storeObserver = remember(state, lifecycleOwner) { object : StoreObserver<OWN_PROPS> {
        override var storeDisposable: Disposable? = null
        override fun shouldReceiveStateChanges()
            = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED).also {
            //NOCOMMIT log
            if( !it) {
                Logger.d("ComposeConnect", "StoreObserver#shouldReceiveStateChanges returned false")
            }
        }
        override fun mapStateToProps(state: GlobalState)
            = mapStateToProps(state, apiProps)
        override fun onStoreStateChanged(nextProps: OWN_PROPS) {
            state.value = nextProps
        }
    }}

    DisposableEffect(storeObserver, lifecycleOwner) {
        val unsubscribe = storeObserver.observe(lifecycleOwner, storeSupplier)
        onDispose {
            Logger.d("ComposeConnect", "DisposableEffect#onDispose - removing subscriber, " +
                    "unsubscribing from the Store") //NOCOMMIT log
            unsubscribe()
        }
    }

    return state
}

/**
 * Subscribes the given observer to the given `Store` within the lifespan of the given `owner`
 * @return unsubscribe
 */
@MainThread
fun <P : OwnProps> StoreObserver<P>.observe(
    owner: LifecycleOwner
    , storeSupplier: () -> Store
) : () -> Unit {
    assertMainThread("observe")
    if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
        // ignore
        Logger.d("ComposeConnect", "observe() called on a DESTROYED state") //NOCOMMIT log
        return {}
    }
    if (this.storeDisposable?.isDisposed == false) {
        // ignore - already subscribed
        Logger.d("ComposeConnect", "observe() called with an already subscribed observer") //NOCOMMIT log
        return {}
    }

    val wrapper = LifecycleBoundObserver(owner, this) {
        //on-destroy
        Logger.d("ComposeConnect", "LifecycleBoundObserver received DESTROYED " +
                "event and so is removing the observer, unsubscribing from the Store") //NOCOMMIT log
        removeObserver(it, storeSupplier)
    }
    Logger.d("ComposeConnect", "observe() is subscribing to Store") //NOCOMMIT log

    //subscribe to the Store
    storeSupplier().subscribe(this)

    //subscribe to the lifecycle
    owner.lifecycle.addObserver(wrapper)

    return { removeObserver(wrapper, storeSupplier) }
}

@MainThread
private fun <P : OwnProps> removeObserver(
    observer: ObserverWrapper<P>
    , storeSupplier: () -> Store
) {
    assertMainThread("removeObserver")
    //check if already unsubscribed and return
    if (observer.mObserver.storeDisposable?.isDisposed != false) {
        return
    }

    storeSupplier().unsubscribe(observer.mObserver)

    observer.detachObserver()
    observer.activeStateChanged(false)
}

internal fun assertMainThread(methodName: String) {
    check(Utils.isOnUiThread()) {
        "Cannot invoke $methodName on a background thread"
    }
}