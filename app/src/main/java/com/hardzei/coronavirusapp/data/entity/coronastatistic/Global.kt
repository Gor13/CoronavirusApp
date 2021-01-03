package com.hardzei.coronavirusapp.data.entity.coronastatistic

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "global_table")
data class Global(

    @field:Json(name = "NewConfirmed")
    var newConfirmed: Int,

    @PrimaryKey
    @field:Json(name = "TotalConfirmed")
    var totalConfirmed: Int,

    @field:Json(name = "NewDeaths")
    var newDeaths: Int,

    @field:Json(name = "TotalDeaths")
    var totalDeaths: Int,

    @field:Json(name = "NewRecovered")
    var newRecovered: Int,

    @field:Json(name = "TotalRecovered")
    var totalRecovered: Int
)
