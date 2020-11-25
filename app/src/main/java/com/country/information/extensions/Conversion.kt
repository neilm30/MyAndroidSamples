package com.country.information.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isConnectedToInternet() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
    it.getNetworkCapabilities(it.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
} ?: false

