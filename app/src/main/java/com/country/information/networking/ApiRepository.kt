package com.country.information.networking

import android.accounts.NetworkErrorException
import com.country.information.networking.model.response.CountryInformation
import com.country.information.networking.retrofit.CountryInfoService
import org.koin.core.KoinComponent

class ApiRepository(private val countryInfoService: CountryInfoService) : CountryInfoEntryPointApi,
    KoinComponent {

    override suspend fun fetchCountryDetails(): CountryInformation {
        countryInfoService.service.fetchCountryDetails().execute().apply {
            if (isSuccessful) {
                return body() ?: throw NetworkErrorException(errorBody().toString())
            }
        }
        throw NetworkErrorException("Request failed.Please try again")
    }

}