package com.country.information.common

/* This is a common data class which maps json response to the user required response*/

data class CountryDetailsResponse(
    val headerTitle: String,
    val rowItems: List<RowResponse>
)

data class RowResponse(
    val title: String?,
    val description: String?,
    val imageUrl: String?
)