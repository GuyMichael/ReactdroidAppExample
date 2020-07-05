package com.guymichael.componentapplicationexample.ui.pager

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.cats.CatsFragment
import com.guymichael.componentapplicationexample.ui.inputsheet.InputSheetFragment
import com.guymichael.componentapplicationexample.ui.qrcode.QRCodeFragment
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.pager.fragment.withFragmentPager
import com.guymichael.reactdroid.extensions.components.tabs.TabsProps
import com.guymichael.reactdroid.extensions.components.tabs.withTabs

class FragmentPagerPage(v : View) : AComponent<EmptyOwnProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: EmptyOwnProps) = EmptyOwnState

    private val cPager = withFragmentPager(R.id.pager)
    private val cTabs = withTabs(R.id.tab_layout, cPager.mView, { tab, pos ->
        tab.text = "Hello $pos"
    })

    override fun render() {
        cPager.onRender(
            ::InputSheetFragment,
            ::CatsFragment,
            ::QRCodeFragment
        )

        cTabs.onRender(TabsProps())
    }
}