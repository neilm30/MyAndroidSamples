package com.country.information.uiscreens

import android.util.Log
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

    // livedata to post success data observed in fragment
    val responseData = MutableLiveData<Pair<List<RowResponse>, String>>()

    // livedata to post error data observed in fragment
    val errorData = MutableLiveData<String>()

    init {
        EspressoIdlingResource.increment() // added for UI testing
        createNetworkJob()
    }

    /**
     * synchronous network call which will run on the IO dispathcer.For asynchronous we can use "async"
     */
    fun createNetworkJob() = launch(Dispatchers.IO) {
        resultOf {
            apiRepository.fetchCountryDetails()
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
        val rowList = countryInformation.rowItems
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

    /**
     * filter to return list of items for  which title ,description or image have values
     * @param rowList list that holds recyclerview item data
     */
    private fun validateRowResponseList(rowList: List<RowResponse>) = rowList.filterNot {
        it.title.isNullOrEmpty() && it.description.isNullOrEmpty() && it.imageUrl.isNullOrEmpty()
    }

    // handle error response and send it to observer
    private fun handleResponseException(exception: Throwable) {
        Log.i(TAG, "Exception message" + exception.printStackTrace())
        errorData.postValue(countryTextMapper.getErrorMessage())
    }

    companion object {
        val TAG = MainViewModel::class.java.canonicalName
    }
}






