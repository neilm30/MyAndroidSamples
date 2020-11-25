package com.country.information.di

import com.country.information.networking.ApiRepository
import com.country.information.networking.CountryInfoEntryPointApi
import com.country.information.networking.retrofit.CountryInfoApi
import com.country.information.networking.retrofit.CountryInfoService
import com.country.information.uiscreens.CountryTextMapper
import com.country.information.uiscreens.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //singleton
    single<CountryInfoEntryPointApi> { ApiRepository(get()) }

    //viewmodels
    viewModel { MainViewModel(get(), get()) }

    //factory classes
    factory { ApiRepository(get()) }

    factory { CountryInfoService() }

    factory { CountryTextMapper(androidContext()) }
}