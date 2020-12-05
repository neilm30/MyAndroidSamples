package com.country.information.uiscreens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.country.information.common.CountryDetailsResponse
import com.country.information.common.RowResponse
import com.country.information.coroutines.OnCancelException
import com.country.information.coroutines.OnError
import com.country.information.coroutines.OnSuccess
import com.country.information.coroutines.ViewModelScope
import com.country.information.networking.CountryInfoEntryPointApi
import com.country.information.utils.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiRepository: CountryInfoEntryPointApi,
    private val countryTextMapper: CountryTextMapper
) : ViewModel(), ViewModelScope {

    // live data to post success data
    val responseData = MutableLiveData<Pair<List<RowResponse>, String>>()

    // live data to post error data
    val errorData = MutableLiveData<String>()

    init {
        EspressoIdlingResource.increment()
        createNetworkJob()
    }

    /**
     * synchronous network call which will run on the IO dispathcer.For asynchronous we can use "async"
     * @param pagelimit the page size to fetch from server for pagination
     */
    fun createNetworkJob(pagelimit: Int = DEFAULT_REQUEST_ITEMS_SIZE) = launch(Dispatchers.IO) {
        resultOf {
            apiRepository.fetchCountryDetails(pagelimit)
        }.let { result ->
            when (result) {
                is OnSuccess<CountryDetailsResponse> -> handleResponseSuccess(result.get())
                is OnCancelException -> {
                    handleResponseException(result.exception)
                }
                is OnError -> {
                    handleResponseException(result.exception)
                }
            }
        }
    }

    /**
     * handle success response and send it to observer
     * @param countryInformation holds the final response
     */
    private fun handleResponseSuccess(countryInformation: CountryDetailsResponse) {
        val rowList = countryInformation.rowsItems
        if (rowList.isNullOrEmpty().not()) {
            responseData.postValue(
                Pair(
                    validateRowResponseList(rowList),
                    countryInformation.headerTitle
                )
            )
        }
        EspressoIdlingResource.decrement()
    }

    private fun validateRowResponseList(rowList: List<RowResponse>) = rowList.filterNot {
        it.title.isNullOrEmpty() && it.description.isNullOrEmpty() && it.imageUrl.isNullOrEmpty()
    }

    // handle error response and send it to observer
    private fun handleResponseException(exception: Throwable) {
        exception.printStackTrace()
        errorData.postValue(countryTextMapper.getErrorMessage())
    }

    companion object {
        const val DEFAULT_REQUEST_ITEMS_SIZE = 10
    }
}








