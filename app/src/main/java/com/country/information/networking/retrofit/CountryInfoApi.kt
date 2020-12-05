package com.country.information.networking.retrofit

import com.country.information.networking.model.response.CountryInformation
import com.country.information.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface CountryInfoApi{
    /** the api does not support any query params to fetch new list of data based on page size
     if needed we can add a query param :    fun fetchCountryDetails(@Query("limit") limit: String,): Call<CountryInformation>
     and pass the page limit to the method from api repository class
    */
    @GET(Constants.COUNTRY_ENDPOINT)
     suspend fun fetchCountryDetails(): Response<CountryInformation>
}