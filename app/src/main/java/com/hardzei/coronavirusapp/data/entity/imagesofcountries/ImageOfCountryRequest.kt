package com.hardzei.coronavirusapp.data.entity.imagesofcountries

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageOfCountryRequest(
    @SerializedName("photos")
    @Expose
    var photos: Photos? = null,

    @SerializedName("stat")
    @Expose
    var stat: String
)
