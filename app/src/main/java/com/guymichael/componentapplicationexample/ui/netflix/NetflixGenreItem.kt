package com.guymichael.componentapplicationexample.ui.netflix

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixGenreData
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactdroid.extensions.components.text.renderText
import com.guymichael.reactdroid.extensions.components.text.withText

class NetflixGenreItem(v: View) : ASimpleComponent<DataProps<NetflixGenreData>>(v) {

    private val cTxtName = v.withText(R.id.netflix_genre_name)

    override fun render() {
        props.data.also { genre ->
            cTxtName.renderText(genre.name)
        }
    }
}