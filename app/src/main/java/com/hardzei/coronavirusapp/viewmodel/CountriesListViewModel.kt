package com.hardzei.coronavirusapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.model.BaseModel
import com.hardzei.coronavirusapp.model.CountriesModel
import io.reactivex.disposables.CompositeDisposable

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
            override fun onSuccess(
                allCountries: List<Country>,
                global: List<Global>
            ) {
                allCountriesMut.postValue(allCountries)
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
        loadData()
    }

    fun loadData() {
        doWork {
            countriesModel.LoadData()
        }
    }


}