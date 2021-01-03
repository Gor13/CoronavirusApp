package com.hardzei.coronavirusapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.model.BaseModel
import com.hardzei.coronavirusapp.model.CountriesModel

class CountriesListViewModel(private val countriesModel: CountriesModel) : BaseViewModel() {

    private var allCountriesMut: MutableLiveData<List<Country>> = MutableLiveData()
    val allCountries: LiveData<List<Country>>
        get() = allCountriesMut

    private var globalMut: MutableLiveData<List<Global>> = MutableLiveData()
    val global: LiveData<List<Global>>
        get() = globalMut

    private var errorsMut: MutableLiveData<String> = MutableLiveData()
    val errors: LiveData<String>
        get() = errorsMut

    init {
        countriesModel.onStatisticChangeListener = object : BaseModel
            .OnStatisticChangeListener {
            override fun onSuccessWithList(allCountries: List<Country>) {
                allCountriesMut.postValue(allCountries)
            }

            override fun onSuccessWithGlobal(global: List<Global>) {
                globalMut.postValue(global)
            }

            override fun onError(errors: String) {
                errorsMut.postValue(errors)
            }
        }
    }

    fun getSortedCountries(sortBy: String) {

        Log.d("TEST_VM", "get sorted country")

        countriesModel.getSortedCountries(sortBy)
    }

    fun loadData() {
        doWork {
            val result = countriesModel.loadData()

            Log.d("TEST_VM_Countries", result.status)
        }
    }
}
