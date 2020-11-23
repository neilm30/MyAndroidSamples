package com.country.information.di

import com.country.information.networking.ApiRepository
import com.country.information.networking.CountryInfoEntryPointApi
import com.country.information.networking.retrofit.CountryInfoApi
import org.koin.dsl.module

val appModule = module {
    //singleton
    single<CountryInfoEntryPointApi>{ ApiRepository(get()) }
    //viewmodels

    //factory classes
}