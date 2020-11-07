package com.hardzei.coronavirusapp.model

import android.util.Log
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.repository.BaseRepository
import com.hardzei.coronavirusapp.data.repository.Repository

class CountriesModel(private val repository: Repository) : BaseModel<CountriesModel.Params, CountriesModel.Result>() {

    private var sortedBy = "in ASC"

    companion object {
        private const val CONST_URL = "https://api.covid19api.com/"
    }

    var onStatisticChangeListener: OnStatisticChangeListener? = null

    init {
        repository.onStatisticChangeListener = object : BaseRepository.OnStatisticChangeListener {
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

    fun getSortedCountries(sortByFromActivity: String) {
        Log.d("TEST_Model", "get sorted country")
        repository.getSortedCountries(sortByFromActivity)
        sortedBy = sortByFromActivity
    }


    class Params()
    data class Result(val result: String)

    override suspend fun LoadData(): Result {
        return Result(repository
                .loadDataWithCountriesStatistic(Repository
                        .Params(sortedBy, CONST_URL)).status)
    }

}