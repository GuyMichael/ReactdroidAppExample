package com.guymichael.componentapplicationexample.ui.pager

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.cats.CatsPage
import com.guymichael.componentapplicationexample.ui.inputsheet.InputSheetPage
import com.guymichael.componentapplicationexample.ui.qrcode.QrCodePage
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.list.layouts.ListIndicatorLayout
import com.guymichael.reactdroid.extensions.components.list.withList

class RecyclerPagerPage(v : View) : AComponent<EmptyOwnProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: EmptyOwnProps) = EmptyOwnState

    private val vListIndicator: ListIndicatorLayout = v.findViewById(R.id.recycler_indicator)

    //this is a non-trivial implementation for a pager - using a standard list (recycler).
    // The advantages are that as a list, we have (currently) many more tweaks we can do,
    // such as list indicator 'dots', page that takes less than 100% width, and more.
    // It is less suitable for interactive/complex pages (just as with nested recyclers) as it
    // may (and currently) have issues with passing the touches correctly
    // (e.g. an item View foreground pressed state)
    private val cListPager = withList(R.id.recycler, RecyclerView.HORIZONTAL).also {
        it.adapter.setSnapEnabled(true)

        //so far for creating a 'pager'. From this point on, only special tweaks
//        it.adapter.customItemWidthFactor = 0.9F   enable to see! (doesn't make sense with indicators..)
        it.adapter.isCyclic = true
        it.adapter.setPageIndicator(vListIndicator, R.layout.list_pager_indicator_item_black)
    }


    init {
        v.findViewById<BottomNavigationView>(R.id.bottom_navigation).also {
            it.setOnNavigationItemSelectedListener { item ->
                cListPager.adapter.let { adapter ->
                    when (item.itemId) {
                        R.id.nav_cats -> adapter.scrollImmediately(adapter.getItemPosition(
                            R.layout.fragment_cats.toString()), true)
                        R.id.nav_input_sheet -> adapter.scrollImmediately(adapter.getItemPosition(
                            R.layout.fragment_input_sheet.toString()), true)
                        R.id.nav_qr -> adapter.scrollImmediately(adapter.getItemPosition(
                            R.layout.fragment_qr.toString()), true)
                    }
                    true //always allow selection
                }
            }
        }
    }



    override fun render() {
        cListPager.onRender(
            R.layout.fragment_cats to ::CatsPage,
            R.layout.fragment_input_sheet to InputSheetPage.Companion::connected,
            R.layout.fragment_qr to QrCodePage.Companion::withPermissions
        )
    }
}