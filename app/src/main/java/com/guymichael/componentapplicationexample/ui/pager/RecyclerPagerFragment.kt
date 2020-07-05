package com.guymichael.componentapplicationexample.ui.pager

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.fragments.BaseFragment

class RecyclerPagerFragment : BaseFragment<EmptyOwnProps, RecyclerPagerPage, EmptyOwnProps>() {
    override fun getLayoutRes() = R.layout.fragment_recycler_pager
    override fun createPageComponent(layout: View) = RecyclerPagerPage(layout)
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = EmptyOwnProps
    override fun createDefaultProps() = EmptyOwnProps
}