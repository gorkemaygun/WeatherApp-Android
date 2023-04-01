package com.example.weatherapp.util

import android.widget.ImageView
import com.bumptech.glide.Glide

object AppUtils {

    fun setGlideImage(image: ImageView, url: String){
        Glide.with(image.context).load(url)
            .thumbnail(0.5f)
            .dontAnimate()
            .into(image)
        // Picasso.get()
        //     .load(url)
        //     .into(image)
    }
}