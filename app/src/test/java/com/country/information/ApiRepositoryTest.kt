package com.country.information

import com.country.information.di.appModule
import com.country.information.networking.ApiRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject


class ApiRepositoryTest : KoinTest {
    val apiRepository: ApiRepository by inject()

     @Test
    fun when_success_check_title_is_not_empty() {
        startKoin {
            modules(appModule)
        }
        runBlocking {
            val countryInformation = apiRepository.fetchCountryDetails()
            Assert.assertTrue(countryInformation.headerTitle.isNotEmpty())
        }
    }

    @Test
    fun when_success_check_listofrows_is_not_empty() {

        runBlocking {
            val countryInformation = apiRepository.fetchCountryDetails()
            Assert.assertTrue(countryInformation.rowItems.isNotEmpty())
        }

    }
}