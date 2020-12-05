package com.country.information.networking.model.response

import com.country.information.common.CountryDetailsResponse
import com.country.information.common.RowResponse
import com.google.gson.annotations.SerializedName

/* data class represents the json respnse*/

data class CountryInformation(

    @SerializedName("title") val title: String = "",
    @SerializedName("rows") val rows: List<Rows> = emptyList()
)

fun CountryInformation.convertTo() = CountryDetailsResponse(
    headerTitle = title,
    rowsItems = rows.asCountryDetails()
)

private fun List<Rows>.asCountryDetails() =
    this.map { rowDetails ->
        RowResponse(
            title = rowDetails.title,
            description = rowDetails.description,
            imageUrl = rowDetails.imageHref
        )
    }





