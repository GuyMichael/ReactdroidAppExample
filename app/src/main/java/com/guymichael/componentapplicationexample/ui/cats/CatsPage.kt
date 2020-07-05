package com.guymichael.componentapplicationexample.ui.cats

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.logic.cats.CatsLogic
import com.guymichael.componentapplicationexample.store.MainStore
import com.guymichael.componentapplicationexample.store.datatype.DataTypeCats
import com.guymichael.componentapplicationexample.withBlockUiProgress
import com.guymichael.kotlinflux.model.GlobalState
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.kotlinreact.onRender
import com.guymichael.promise.letIf
import com.guymichael.reactdroid.core.model.ASimpleViewComponent
import com.guymichael.reactdroid.extensions.components.list.adapter.model.RecyclerComponentAdapterScrollListener
import com.guymichael.reactdroid.extensions.components.list.model.ListItemProps
import com.guymichael.reactdroid.extensions.components.list.model.ListProps
import com.guymichael.reactdroid.extensions.components.list.withList
import com.guymichael.reactdroidflux.model.connect
import com.guymichael.reactdroidflux.model.withDataManager

/**
 * Shows images of cats with paging - fetches more as the list reaches bottom
 */
class CatsPage(v: View) : ASimpleViewComponent<EmptyOwnProps>(v) {

    /* init child components */

    private val cList = connect(
        //wrap list with a data manager (HOC)
        withDataManager(
            //component
            withList(R.id.cats_list).also {
                //load (10) more cats when user scrolls to bottom (paging)
                it.adapter.setOnReachingBottomListener(object : RecyclerComponentAdapterScrollListener.OnReachingBottomListener {
                    override fun onReachingBottom(lastVisibleItemPos: Int, visibleItemCount: Int) {
                        CatsLogic.fetchAndDispatchNextCats(10).execute()
                    }
                })
            }
            //load 15 cats on list mount (page view), show loader only if no cats are shown
            , { props -> CatsLogic.fetchAndDispatchNextCats(15).letIf({ props.items.isEmpty() }) {
                it.withBlockUiProgress(this@CatsPage)
            }}
            , shouldReloadData = { _, _ -> false  } //never reload - only load on mount (page view)
        )

        //connect
        , ::mapStateToCats          //because this method's second argument is EmptyOwnProps,
                                    // the connect() method infers it so that our connected component (HOC)
                                    // will receive EmptyOwnProps as external/parent/api props
                                    // instead of the inner component's (CList) ListProps
        , { MainStore }
    )




    /* render, always last */
    override fun render() {
        //call all children's onRender(props)
        cList.onRender() //extension for onRender(EmptyOwnProps) - this connected list doesn't need external props
    }
}






/* logic methods outside of class - pure functional without 'this'/context */

private fun mapStateToCats(state: GlobalState, props: EmptyOwnProps): ListProps {
    return ListProps(DataTypeCats.getAll(state)
        ?.map { ListItemProps(it.id, R.layout.item_cat, DataProps(it), ::CatItem) }
        ?.sortedBy { it.props.data.id }
        ?: emptyList()
    )
}