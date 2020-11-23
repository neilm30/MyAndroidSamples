package com.country.information.networking.retrofit

import com.country.information.BuildConfig
import com.country.information.networking.httpclient.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryInfoService {
         val service: CountryInfoApi by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.createClient())
                .build()
                .create(CountryInfoApi::class.java)
        }
}
