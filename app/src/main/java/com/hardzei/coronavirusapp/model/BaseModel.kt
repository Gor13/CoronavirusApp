package com.hardzei.coronavirusapp.model

import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global

abstract class BaseModel<Params, Result> {

    abstract suspend fun LoadData(): Result

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
                country: Country
        )

        fun onGetLinkSuccess(
                link: String
        )

        fun onError(errors: String)
    }

    interface OnStatisticChangeListener {
        fun onSuccess(
                allCountries: List<Country>,
                global: List<Global>
        )

        fun onError(errors: String)
    }
}