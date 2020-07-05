package com.guymichael.componentapplicationexample.ui.translation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guymichael.componentapplicationexample.R
import com.guymichael.kotlinreact.model.EmptyOwnProps

//example for usage without a ComponentFragment and outside of an AComponent context
class TranslationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        : View {

        return inflater.inflate(R.layout.fragment_translation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TranslationPage(view).onRender(EmptyOwnProps)
    }
}