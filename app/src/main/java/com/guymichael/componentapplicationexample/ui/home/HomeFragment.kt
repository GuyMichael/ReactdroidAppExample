package com.guymichael.componentapplicationexample.ui.home

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.fragments.BaseFragment
import com.guymichael.reactdroid.core.model.AComponent

class HomeFragment : BaseFragment<EmptyOwnProps, AComponent<EmptyOwnProps, *, *>, EmptyOwnProps>() {
    override fun getLayoutRes() = R.layout.fragment_home
    override fun createDefaultProps() = EmptyOwnProps
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = EmptyOwnProps
    override fun createPageComponent(layout: View) = HomePage.connected(layout)
}