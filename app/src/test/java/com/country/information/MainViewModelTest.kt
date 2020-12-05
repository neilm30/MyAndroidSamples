package com.country.information

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.country.information.common.CountryDetailsResponse
import com.country.information.common.RowResponse
import com.country.information.networking.ApiRepository
import com.country.information.networking.CountryInfoEntryPointApi
import com.country.information.networking.retrofit.CountryInfoService
import com.country.information.uiscreens.CountryTextMapper
import com.country.information.uiscreens.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import org.koin.test.KoinTest
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class MainViewModelTest : KoinTest {
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var mainCoroutineRule: TestRule = InstantTaskExecutorRule()

    private var apiRepositoryMock: CountryInfoEntryPointApi =
        Mockito.mock(ApiRepository::class.java)
    private var countryInfoServiceMock: CountryInfoService =
        Mockito.mock(CountryInfoService::class.java)
    private var countryTextMapperMock: CountryTextMapper =
        Mockito.mock(CountryTextMapper::class.java)

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun when_success_check_title_is_not_empty() {

        runBlockingTest {
            Mockito.`when`(apiRepositoryMock.fetchCountryDetails(10)).thenReturn(countrydetails)
            mainViewModel = MainViewModel(apiRepositoryMock, countryTextMapperMock)
            Thread.sleep(DELAY_IN_MILLIS)
            Assert.assertEquals(
                mainViewModel.responseData.value?.second,
                countrydetails.headerTitle
            )
        }

    }

    @Test
    fun when_success_check_list_is_not_empty() {

        runBlockingTest {
            Mockito.`when`(apiRepositoryMock.fetchCountryDetails(10)).thenReturn(countrydetails)
            mainViewModel = MainViewModel(apiRepositoryMock, countryTextMapperMock)
            Thread.sleep(DELAY_IN_MILLIS)
            Assert.assertNotNull(mainViewModel.responseData.value?.first.isNullOrEmpty().not())
            Assert.assertEquals(
                mainViewModel.responseData.value?.first?.first()?.description,
                countrydetails.rowsItems.first().description
            )
        }

    }

    val countrydetails = CountryDetailsResponse(
        "About Canada",
        listOf(
            RowResponse(
                title = "Beavers",
                description = "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                imageUrl = "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
            )
        )

    )

    companion object {
        val DELAY_IN_MILLIS = 7000L
    }
}
