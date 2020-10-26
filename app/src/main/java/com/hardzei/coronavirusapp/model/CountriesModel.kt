package com.hardzei.coronavirusapp.model

import android.util.Log
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.repository.Repository

class CountriesModel(private val repository: Repository) {

    var onStatisticChangeListener: OnStatisticChangeListener? = null

    init {
        repository.onStatisticChangeListener = object : Repository.OnStatisticChangeListener {
            override fun onSuccess(
                allCountries: List<Country>,
                global: List<Global>
            ) {
                onStatisticChangeListener?.onSuccess(allCountries, global)
            }

            override fun onError(errors: String) {
                onStatisticChangeListener?.onError(errors)
            }
        }
    }

    fun getSortedCountries(sortBy: String) {
        Log.d("TEST_Model", "get sorted country")
        repository.getSortedCountries(sortBy)
    }

    fun loadData() = repository.loadDataWithCountriesStatistic()

    interface OnStatisticChangeListener {
        fun onSuccess(
            allCountries: List<Country>,
            global: List<Global>
        )

        fun onError(errors: String)
    }
}