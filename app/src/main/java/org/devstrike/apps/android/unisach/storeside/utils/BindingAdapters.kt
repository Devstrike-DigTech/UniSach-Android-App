/*
 * Copyright (c) 2022.
 * Richard Uzor
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Universal function to implement loading image with glide and making it acceptable as view xml attribute
 * This can also be used in other classes to load the image from internet using just the function name
 * Created by Richard Uzor  on 24/12/2022
 */

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) {
    if (url.isNullOrEmpty()) return
    Glide.with(this).load(url).into(this)
}