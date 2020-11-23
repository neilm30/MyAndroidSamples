package com.country.information.networking.retrofit

import com.country.information.networking.model.response.CountryInformation
import com.country.information.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface CountryInfoApi{
    @GET(Constants.COUNTRY_ENDPOINT)
    fun fetchCountryDetails(): Call<CountryInformation>
}