package com.guymichael.componentapplicationexample.ui.cats

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactiveapp.fragments.BaseFragment

class CatsFragment : BaseFragment<EmptyOwnProps, CatsPage, EmptyOwnProps>() {
    override fun getLayoutRes() = R.layout.fragment_cats
    override fun createPageComponent(layout: View) = CatsPage(layout)
    override fun createDefaultProps() = EmptyOwnProps
    override fun mapFragmentPropsToPageProps(props: EmptyOwnProps) = props
}