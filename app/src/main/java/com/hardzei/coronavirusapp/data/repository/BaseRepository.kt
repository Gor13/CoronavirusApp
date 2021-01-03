package com.hardzei.coronavirusapp.data.repository

import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global

interface BaseRepository<Params, Result> {

    suspend fun loadListWithLinksOfImages(params: Params): Result
    suspend fun loadDataWithCountriesStatistic(params: Params): Result

    interface OnStatisticChangeListener {
        fun onSuccessWithList(
            allCountries: List<Country>,
        )

        fun onSuccessWithGlobal(
            global: List<Global>,
        )

        fun onError(errors: String)
    }

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
            country: Country,
        )

        fun onError(errors: String)
    }
}
