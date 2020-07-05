package com.guymichael.componentapplicationexample.ui.countries

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.network.client.countries.data.CountryData
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactdroid.extensions.components.text.renderText
import com.guymichael.reactdroid.extensions.components.text.renderTextOrGone
import com.guymichael.reactdroid.extensions.components.text.withText

class CountryItem(v: View) : ASimpleComponent<DataProps<CountryData>>(v) {

    private val cTxtName = v.withText(R.id.country_name)
    private val cTxtNativeName = v.withText(R.id.country_native_name)
    private val cTxtRegion = v.withText(R.id.country_region)
    private val cTxtCapital = v.withText(R.id.country_capital)
    private val cTxtPopulation = v.withText(R.id.country_population)

    override fun render() {
        props.data.also { country ->
            cTxtName.renderText(country.name)
            cTxtNativeName.renderTextOrGone(country.nativeName?.let { "($it)" })
            cTxtRegion.renderTextOrGone(country.region)
            cTxtCapital.renderText(country.capital)
            cTxtPopulation.renderTextOrGone(country.population?.toString())
        }
    }
}