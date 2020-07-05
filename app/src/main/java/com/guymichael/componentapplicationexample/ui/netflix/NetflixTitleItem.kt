package com.guymichael.componentapplicationexample.ui.netflix

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.network.client.netflix.data.NetflixTitleData
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.reactiveapp.reactdroid.components.image.withImage
import com.guymichael.reactiveapp.utils.AndroidUtils
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactdroid.extensions.components.text.renderText
import com.guymichael.reactdroid.extensions.components.text.withText

class NetflixTitleItem(v: View) : ASimpleComponent<DataProps<NetflixTitleData>>(v) {
    private val cTxtName = v.withText(R.id.netflix_title_name)
    private val cTxtReleaseYear = v.withText(R.id.netflix_title_year)
    private val cTxtRating = v.withText(R.id.netflix_title_rating)
    private val cTxtDurationAndType = v.withText(R.id.netflix_title_type_and_duration)
    private val cImg = v.withImage(R.id.netflix_title_img)

    init {
        cImg.mView.setOnLongClickListener {
            AndroidUtils.toast(it.context, "Image (long) clicked: ${props.data.title}")
            true
        }
    }

    override fun render() {
        props.data.also { netflix ->

            cTxtName.renderText(netflix.title)
            cTxtReleaseYear.renderText("(${netflix.released})")
            cTxtRating.renderText(netflix.rating.toString())
            cTxtDurationAndType.renderText("${netflix.runtime} (${netflix.type})")

            cImg.render(netflix.image)
            cImg.mView.contentDescription = netflix.title //use example of direct view manipulation
                                                          // normally you'd expand the component/props
                                                          // to include that
        }
    }
}