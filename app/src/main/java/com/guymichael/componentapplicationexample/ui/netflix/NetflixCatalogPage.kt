package com.guymichael.componentapplicationexample.ui.netflix

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guymichael.apromise.APromise
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.logic.netflix.NetflixLogic
import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixGenreData
import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixTitleData
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeNetflixGenre
import com.guymichael.componentapplicationexample.store.datatype.DataTypeNetflixTitle
import com.guymichael.componentapplicationexample.withBlockUiProgress
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.kotlinreact.model.props.LongProps
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.core.model.AViewComponent
import com.guymichael.reactdroid.extensions.components.list.model.ListItemProps
import com.guymichael.reactdroid.extensions.components.list.model.ListProps
import com.guymichael.reactdroid.extensions.components.list.withList
import com.guymichael.reactdroidflux.model.connect
import com.guymichael.reactdroidflux.model.withDataManager
import com.guymichael.reactiveapp.utils.AndroidUtils

/**
 * Shows Netflix genres OR titles according to (own) state.
 *
 * NOTICE: Generally showing different content on same page is wrong in Android.
 *  It over-complicates things both for the user and for the developer and is prone to errors.
 *  This fragment is just for `Reactdroid` capabilities showoff
 */
class NetflixCatalogPage(v: View) : AViewComponent<LongProps, NetflixCatalogOwnState>(v) {
    override fun createInitialState(props: LongProps) = NetflixCatalogOwnState(props.value)

    /* init child components */

    private val cList = connect(
        withDataManager(
            //component
            withList(R.id.netflix_list).also {
                it.adapter.onItemClick(::onItemClick)
                it.adapter.setDividers(R.dimen.cardList_divider, R.dimen.cardList_divider_top_bottom)
            }
            , { fetchDataAccordingToState(ownState, this@NetflixCatalogPage) }
        )

        //connect
        , ::mapStateToListProps
        , { MainStore }
    )





    /* Lifecycle */

    //NOTICE: All logic in this method is one good example to why we won't actually do two lists in one page
    // * actually, for a complete experience, we'd need to save the list index position of the clicked
    //   genre! Just don't do two lists in one page.
    override fun componentDidUpdate(prevProps: LongProps, prevState: NetflixCatalogOwnState,
                                    snapshot: Any?
    ) {
        //scroll list to top when changing between genres and titles state
        if (this.ownState.genreId != prevState.genreId) {
            //genre/title list state has changed (or genre has changed)
            // -> scroll list to top
            cList.mView.post { //wait for RecyclerView to update from the prev render call
                cList.mView.scrollToPosition(0)
            }
        }
    }





    /* Callbacks/listeners */

    private fun onItemClick(parent: RecyclerView.Adapter<*>, view: View
        , props: ListItemProps<*>, index: Int
    ): Boolean {

        @Suppress("UNCHECKED_CAST")
        return if (ownState.genreId == null) {
            //genre click
            setState(NetflixCatalogOwnState(
                genreId = (props.props as DataProps<NetflixGenreData>).data.titleIds.firstOrNull()
            ))
            true
        } else {
            //title click
            AndroidUtils.toast(mView.context,
                "Clicked: ${(props.props as DataProps<NetflixTitleData>).data.title}")
            true
        }
    }





    /* Logic methods which require 'this' for some reason */





    /* render, always last */

    override fun render() {
        cList.onRender(LongProps(ownState.genreId))
    }
}






/* logic methods outside of class - pure functional without 'this'/context */

private fun mapStateToGenres(state: GlobalState): ListProps {
    return ListProps(DataTypeNetflixGenre.getAll(state)
        ?.map { ListItemProps(it.name, R.layout.item_netflix_genre, DataProps(it), ::NetflixGenreItem) }
        ?.sortedBy { it.props.data.name }
        ?: emptyList()
    )
}

private fun mapStateToTitles(state: GlobalState): ListProps {
    return ListProps(DataTypeNetflixTitle.getAll(state)
        ?.map { ListItemProps(it.title, R.layout.item_netflix_title, DataProps(it), ::NetflixTitleItem) }
        ?.sortedBy { it.props.data.title }
        ?: emptyList()
    )
}

private fun mapStateToListProps(state: GlobalState, genreId: LongProps): ListProps {
    return if (genreId.value == null) {
        mapStateToGenres(state)
    } else {
        mapStateToTitles(state)
    }
}

private fun fetchDataAccordingToState(localState: NetflixCatalogOwnState
        , componentForBlockUiProgress: AComponent<*, *, *>
        , globalState: GlobalState = MainStore.state
    ): APromise<Unit> {

    return if (localState.genreId == null) {
        //fetch genres if no cache
        NetflixLogic.loadOrFetchGenres()
            .withBlockUiProgress(componentForBlockUiProgress)

    } else {
        //fetch titles if no cache
        // note: we can to it like the genres (loadOrFetchTitles), but this is just for
        // demonstrating an alternative way
        if (DataTypeNetflixTitle.exists(globalState)) {
            APromise.of()
        } else {
            NetflixLogic.fetchAndDispatchTitles().thenMap { Unit }
                .withBlockUiProgress(componentForBlockUiProgress)
        }
    }
}