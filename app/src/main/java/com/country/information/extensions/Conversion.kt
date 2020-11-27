package com.country.information.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun Context.isConnectedToInternet() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
    it.getNetworkCapabilities(it.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
} ?: false

fun Context.shareImageByUrl(pictureUrl: String,  imageView: ImageView) {
    Glide.with(this).load(pictureUrl).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.visibility = View.VISIBLE
            return false
        }

    }).fitCenter().into(imageView)
}

