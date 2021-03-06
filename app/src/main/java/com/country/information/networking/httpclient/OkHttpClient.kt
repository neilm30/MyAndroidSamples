package com.country.information.networking.httpclient

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/* represents http client using okhttp */

object OkHttpClient {
    fun createClient() = OkHttpClient.Builder()
        .callTimeout(20, TimeUnit.SECONDS).build()
}
