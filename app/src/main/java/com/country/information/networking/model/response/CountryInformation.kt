package com.country.information.networking.model.response

import com.google.gson.annotations.SerializedName

/* data class represents the json respnse*/

data class CountryInformation(

    @SerializedName("title") val title: String,
    @SerializedName("rows") val rows: List<Rows>
){
  companion object
}