package com.hardzei.coronavirusapp.model

import android.util.Log
import com.hardzei.coronavirusapp.SORTED_BY_ALPHABET
import com.hardzei.coronavirusapp.SUCCESS_STATUS
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.repository.BaseRepository
import com.hardzei.coronavirusapp.data.repository.Repository

class CountriesModel(private val repository: Repository) :
    BaseModel<CountriesModel.Params, CountriesModel.Result> {

    private var sortedBy = SORTED_BY_ALPHABET

    var onStatisticChangeListener: BaseModel.OnStatisticChangeListener? = null

    init {
        repository.onStatisticChangeListener = object : BaseRepository.OnStatisticChangeListener {
            override fun onSuccessWithList(allCountries: List<Country>) {
                onStatisticChangeListener?.onSuccessWithList(allCountries)
            }

            override fun onSuccessWithGlobal(global: List<Global>) {
                onStatisticChangeListener?.onSuccessWithGlobal(global)
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

    class Params
    data class Result(val status: String)

    override suspend fun loadData(): Result {
        val result = Result(
            repository
                .loadDataWithCountriesStatistic(
                    Repository
                        .Params(sortedBy)
                ).status
        )
        if (result.status == SUCCESS_STATUS) {
            Log.d("TEST_CountrModel", SUCCESS_STATUS)
            repository.getSortedCountries(sortedBy)
        }
        return result
    }
}
