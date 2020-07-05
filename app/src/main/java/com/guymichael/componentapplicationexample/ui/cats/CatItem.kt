package com.guymichael.componentapplicationexample.ui.cats

import android.view.View
import com.guymichael.componentapplicationexample.R
import com.guymichael.componentapplicationexample.network.client.cats.data.CatData
import com.guymichael.kotlinreact.model.props.DataProps
import com.guymichael.reactdroid.core.model.ASimpleComponent
import com.guymichael.reactiveapp.reactdroid.components.image.withImage

class CatItem(v: View) : ASimpleComponent<DataProps<CatData>>(v) {

    private val cImg = v.withImage(R.id.cat_img)

    override fun render() {
        props.data.also { cat ->
            //THINK use Picasso directly and force the image size according to provided data (cat) width/height
            cImg.render(cat.url, R.drawable.placeholder_item_img)
        }
    }
}