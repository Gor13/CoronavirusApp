package com.hardzei.coronavirusapp.data.entity.coronastatistic

import com.squareup.moshi.Json

data class CountriesRequest(
        @field:Json(name = "Global")
        var global: Global,

        @field:Json(name = "Countries")
        var countries: List<Country>,

        @field:Json(name = "Date")
        var date: String
)