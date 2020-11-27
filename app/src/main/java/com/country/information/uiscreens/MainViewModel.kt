package com.country.information.uiscreens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.country.information.coroutines.OnCancelException
import com.country.information.coroutines.OnError
import com.country.information.coroutines.OnSuccess
import com.country.information.coroutines.ViewModelScope
import com.country.information.networking.CountryInfoEntryPointApi
import com.country.information.networking.model.response.CountryInformation
import com.country.information.networking.model.response.Rows
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent

class MainViewModel(private val apiRepository: CountryInfoEntryPointApi, private val countryTextMapper: CountryTextMapper) : ViewModel(), ViewModelScope {

    // live data to post success data
    val responseData = MutableLiveData<Pair<List<Rows>, String>>()

    // live data to post error data
    val errorData = MutableLiveData<String>()

     init {
         createNetworkJob()
     }

    /*synchronous network call which will run on the IO dispathcer.For asynchronous we can use "async"*/

     fun createNetworkJob(pagelimit: Int = DEFAULT_REQUEST_ITEMS_SIZE) = launch(Dispatchers.IO) {
        resultOf {
            apiRepository.fetchCountryDetails(pagelimit)
        }.let { result ->
                when (result) {
                    is OnSuccess<CountryInformation> -> handleResponseSuccess(result.get())
                    is OnCancelException -> {
                        handleResponseException(result.exception)
                    }
                    is OnError -> {
                        handleResponseException(result.exception)
                    }
                }
        }
    }

    // handle success response and send it to observer
    private fun handleResponseSuccess(countryInformation: CountryInformation) {
        val rowList = countryInformation.rows
        if (rowList.isNullOrEmpty().not()) {
            responseData.postValue(Pair(validateRowResponseList(rowList), countryInformation.title))
        }
    }

    private fun validateRowResponseList(rowList: List<Rows>) = rowList.filterNot {
        it.title.isNullOrEmpty() && it.description.isNullOrEmpty() && it.imageHref.isNullOrEmpty()
    }

    // handle error response and send it to observer
    private fun handleResponseException(exception: Throwable) {
        exception.printStackTrace()
        errorData.postValue(countryTextMapper.getErrorMessage())
    }

    companion object{
        const val DEFAULT_REQUEST_ITEMS_SIZE = 10
    }
}








