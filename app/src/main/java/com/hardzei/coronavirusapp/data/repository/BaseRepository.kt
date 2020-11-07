package com.hardzei.coronavirusapp.data.repository

import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global

abstract class BaseRepository<Params, Result> {

    abstract suspend fun loadListWithLinksOfImages(params: Params): Result
    abstract suspend fun loadDataWithCountriesStatistic(params: Params): Result

    interface OnStatisticChangeListener {
        fun onSuccess(
                allCountries: List<Country>,
                global: List<Global>
        )

        fun onError(errors: String)
    }

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
                country: Country
        )

        fun onError(errors: String)
    }
}