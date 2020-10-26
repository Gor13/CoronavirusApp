package com.hardzei.coronavirusapp.data.entity.coronastatistic

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountriesRequest(
    @SerializedName("Global")
    @Expose
    var global: Global,

    @SerializedName("Countries")
    @Expose
    var countries: List<Country>,

    @SerializedName("Date")
    @Expose
    var date: String
)