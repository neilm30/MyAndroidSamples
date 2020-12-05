package com.country.information.networking

import android.accounts.NetworkErrorException
import com.country.information.common.CountryDetailsResponse
import com.country.information.networking.model.response.convertTo
import com.country.information.networking.retrofit.CountryInfoService
import org.koin.core.KoinComponent

class ApiRepository(private val countryInfoService: CountryInfoService) : CountryInfoEntryPointApi,
    KoinComponent {

    override suspend fun fetchCountryDetails(pageLimit: Int): CountryDetailsResponse {
        val response = countryInfoService.service.fetchCountryDetails()
        if (response.isSuccessful) {
            response.body()?.let {
                return it.convertTo()
            } ?: throw NetworkErrorException(response.message())
        }
        throw NetworkErrorException("Request failed.Please try again")
    }
}