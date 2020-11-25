package com.country.information.networking.retrofit

import com.country.information.BuildConfig
import com.country.information.networking.httpclient.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/* represents retrofit builder creation */

class CountryInfoService {
    val service: CountryInfoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()
            .create(CountryInfoApi::class.java)
    }

    private fun createHttpClient() = okhttp3.OkHttpClient.Builder()
        .callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS).build()

    companion object {
        const val NETWORK_TIMEOUT = 20L
    }
}
