package com.hardzei.coronavirusapp.model

import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global

interface BaseModel<Params, Result> {

    suspend fun loadData(): Result

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
            country: Country,
        )

        fun onGetLinkSuccess(
            link: String,
        )

        fun onError(errors: String)
    }

    interface OnStatisticChangeListener {
        fun onSuccessWithList(
            allCountries: List<Country>,
        )

        fun onSuccessWithGlobal(
            global: List<Global>,
        )

        fun onError(errors: String)
    }
}
