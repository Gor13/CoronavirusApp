package com.hardzei.coronavirusapp.data.entity.coronastatistic

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "global_table")
data class Global(

    @SerializedName("NewConfirmed")
    @Expose
    var newConfirmed: Int,

    @PrimaryKey
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
    var totalRecovered: Int

)