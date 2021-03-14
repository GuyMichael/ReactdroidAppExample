package com.guymichael.componentapplicationexample.ui.components.jetpackcompose.core.model

import com.guymichael.kotlinreact.model.OwnProps

data class ValueProps<T>(val value: T) : OwnProps() {
    override fun getAllMembers() = listOf(value)
}