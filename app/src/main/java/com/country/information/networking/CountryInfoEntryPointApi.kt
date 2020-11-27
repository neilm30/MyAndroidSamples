package com.country.information.networking

import com.country.information.networking.model.response.CountryInformation
import com.country.information.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface CountryInfoEntryPointApi{
    suspend fun fetchCountryDetails(pageLimit: Int): CountryInformation
}