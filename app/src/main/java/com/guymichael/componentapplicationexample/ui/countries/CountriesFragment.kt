package com.guymichael.componentapplicationexample.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps

//an example of a plain fragment (no use of ComponentFragment - discouraged)
class CountriesFragment : Fragment() {

    private lateinit var cCountriesPage: CountriesPage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        : View {

        return inflater.inflate(R.layout.fragment_countries, container, false).also {
            cCountriesPage = CountriesPage(it)
            cCountriesPage.onRender(EmptyOwnProps)
        }
    }
}