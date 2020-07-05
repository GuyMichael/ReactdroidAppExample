package com.guymichael.componentapplicationexample.ui.inputsheet

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.fragments.BaseFragment
import com.guymichael.reactdroid.core.model.AComponent

class InputSheetFragment : BaseFragment<EmptyOwnProps, AComponent<EmptyOwnProps, *, *>, EmptyOwnProps>() {
    override fun getLayoutRes() = R.layout.fragment_input_sheet
    override fun createDefaultProps() = EmptyOwnProps
    override fun createPageComponent(layout: View) = InputSheetPage.connected(layout)
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = EmptyOwnProps
}