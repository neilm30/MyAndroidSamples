package com.country.information.networking

import com.country.information.common.CountryDetailsResponse

interface CountryInfoEntryPointApi{
    suspend fun fetchCountryDetails(pageLimit: Int): CountryDetailsResponse
}