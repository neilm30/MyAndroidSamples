package com.country.information.networking

import android.accounts.NetworkErrorException
import com.country.information.networking.model.response.CountryInformation
import com.country.information.networking.retrofit.CountryInfoApi
import com.country.information.networking.retrofit.CountryInfoService
import retrofit2.Call

class ApiRepository(private val countryInfoService: CountryInfoService): CountryInfoEntryPointApi{

    override suspend fun fetchCountryDetails(): CountryInformation {
        countryInfoService.service.fetchCountryDetails().execute().apply {
          if(isSuccessful){
           return body()?: throw NetworkErrorException()
          }
       }
        throw NetworkErrorException("Request failed.Please try again")
    }

}