package com.hardzei.coronavirusapp.data.entity.coronastatistic

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "country_table")
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @SerializedName("Country")
    @Expose
    var country: String,

    @SerializedName("CountryCode")
    @Expose
    var countryCode: String,

    @SerializedName("Slug")
    @Expose
    var slug: String,

    @SerializedName("NewConfirmed")
    @Expose
    var newConfirmed: Int,

    @SerializedName("TotalConfirmed")
    @Expose
    var totalConfirmed: Int,

    @SerializedName("NewDeaths")
    @Expose
    var newDeaths: Int,

    @SerializedName("TotalDeaths")
    @Expose
    var totalDeaths: Int,

    @SerializedName("NewRecovered")
    @Expose
    var newRecovered: Int,

    @SerializedName("TotalRecovered")
    @Expose
    var totalRecovered: Int,

    @SerializedName("Date")
    @Expose
    var date: String
)