package com.guymichael.componentapplicationexample.ui

import android.view.ViewGroup
import com.guymichael.kotlinreact.model.EmptyOwnProps
import com.guymichael.reactdroid.core.model.ASimpleComponent

class EmptyPage(v: ViewGroup) : ASimpleComponent<EmptyOwnProps>(v) {
    override fun render() {}
}