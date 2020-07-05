package com.guymichael.componentapplicationexample.ui.netflix

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.props.LongProps
import com.guymichael.reactiveapp.fragments.BaseFragment

class NetflixCatalogFragment : BaseFragment<LongProps, NetflixCatalogPage, LongProps>() {
    override fun getLayoutRes() = R.layout.fragment_netflix
    override fun createPageComponent(layout: View) = NetflixCatalogPage(layout)
    override fun createDefaultProps() = LongProps(null)
    override fun mapFragmentPropsToPageProps(props: LongProps) = props
}