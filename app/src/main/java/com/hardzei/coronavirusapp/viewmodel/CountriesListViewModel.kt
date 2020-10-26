package com.hardzei.coronavirusapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.model.CountriesModel
import io.reactivex.disposables.CompositeDisposable

class CountriesListViewModel(private val countriesModel: CountriesModel) : ViewModel() {

    private var compositeDisposable = CompositeDisposable()

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
        countriesModel.onStatisticChangeListener = object : CountriesModel
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

        loadData()
    }

    fun getSortedCountries(sortBy: String) {
        Log.d("TEST_VM", "get sorted country")
        countriesModel.getSortedCountries(sortBy)
    }

    fun loadData() {
        compositeDisposable.addAll(countriesModel.loadData())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}