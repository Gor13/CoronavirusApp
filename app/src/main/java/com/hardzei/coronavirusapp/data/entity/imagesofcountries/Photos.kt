package com.hardzei.coronavirusapp.data.entity.imagesofcountries

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("page")
    @Expose
    var page: Int,

    @SerializedName("pages")
    @Expose
    var pages: Int,

    @SerializedName("perpage")
    @Expose
    var perpage: Int,

    @SerializedName("total")
    @Expose
    var total: String,

    @SerializedName("photo")
    @Expose
    var photo: List<Photo>? =
        null
)
