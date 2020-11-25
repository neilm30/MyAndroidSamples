package com.country.information.uiscreens

import android.content.Context
import com.country.information.R

/* class that will have all mapping for  text, color etc that need to be passed from viewmodel to ui
We shpould never have context reference in viewmodel
* */
class CountryTextMapper(private val context: Context) {

    fun getErrorMessage() = context.getString(R.string.error_message)
}