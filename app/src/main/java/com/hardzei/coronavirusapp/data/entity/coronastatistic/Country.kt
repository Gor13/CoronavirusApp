package com.hardzei.coronavirusapp.data.entity.coronastatistic

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "country_table")
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @field:Json(name = "Country")
    var country: String,

    @field:Json(name = "CountryCode")
    var countryCode: String,

    @field:Json(name = "Slug")
    var slug: String,

    @field:Json(name = "NewConfirmed")
    var newConfirmed: Int,

    @field:Json(name = "TotalConfirmed")
    var totalConfirmed: Int,

    @field:Json(name = "NewDeaths")
    var newDeaths: Int,

    @field:Json(name = "TotalDeaths")
    var totalDeaths: Int,

    @field:Json(name = "NewRecovered")
    var newRecovered: Int,

    @field:Json(name = "TotalRecovered")
    var totalRecovered: Int,

    @field:Json(name = "Date")
    var date: String
)
