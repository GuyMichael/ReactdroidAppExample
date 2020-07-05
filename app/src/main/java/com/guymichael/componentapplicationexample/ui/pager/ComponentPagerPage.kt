package com.guymichael.componentapplicationexample.ui.pager

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.ui.cats.CatsPage
import com.guymichael.componentapplicationexample.ui.inputsheet.InputSheetPage
import com.guymichael.componentapplicationexample.ui.qrcode.QrCodePage
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.kotlinreact.model.EmptyOwnState
import com.guymichael.reactdroid.core.model.AComponent
import com.guymichael.reactdroid.extensions.components.pager.component.withPager
import com.guymichael.reactdroid.extensions.components.tabs.TabsProps
import com.guymichael.reactdroid.extensions.components.tabs.withTabs

class ComponentPagerPage(v : View) : AComponent<EmptyOwnProps, EmptyOwnState, View>(v) {
    override fun createInitialState(props: EmptyOwnProps) = EmptyOwnState

    private val cPager = withPager(R.id.pager)
    private val cTabs = withTabs(R.id.tab_layout, cPager.mView, { tab, pos ->
        tab.text = "Hello $pos"
    })

    override fun render() {
        cPager.onRender(
            R.layout.fragment_cats to ::CatsPage,
            R.layout.fragment_input_sheet to InputSheetPage.Companion::connected,
            R.layout.fragment_qr to QrCodePage.Companion::withPermissions
        )

        cTabs.onRender(TabsProps()) //better to be placed after pager render
    }
}