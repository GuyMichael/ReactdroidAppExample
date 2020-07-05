package com.guymichael.componentapplicationexample.ui.pager

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.fragments.BaseFragment

class ComponentPagerFragment : BaseFragment<EmptyOwnProps, ComponentPagerPage, EmptyOwnProps>() {
    override fun getLayoutRes() = R.layout.fragment_pager
    override fun createPageComponent(layout: View) = ComponentPagerPage(layout)
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = EmptyOwnProps
    override fun createDefaultProps() = EmptyOwnProps
}