package com.hardzei.coronavirusapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.model.BaseModel
import com.hardzei.coronavirusapp.model.CountryDetailModel

class DetailCountryViewModel(private val countryDetailModel: CountryDetailModel) : BaseViewModel() {

    private val _countryDetail: MutableLiveData<Country> = MutableLiveData()
    val countryDetail: LiveData<Country>
        get() = _countryDetail

    private var _countryLink: MutableLiveData<String> = MutableLiveData()
    val countryLink: LiveData<String>
        get() = _countryLink

    private val _errors: MutableLiveData<String> = MutableLiveData()
    val errors: LiveData<String>
        get() = _errors

    init {
        countryDetailModel
            .onDetailCountryChangeListener = object : BaseModel.OnDetailCountryChangeListener {
            override fun onGetCountrySuccess(country: Country) {
                _countryDetail.postValue(country)
                loadData()
            }

            override fun onGetLinkSuccess(link: String) {
                _countryLink.postValue(link)
            }

            override fun onError(errors: String) {
                _errors.postValue(errors)
            }
        }
    }

    fun getCountryById(id: Int) = countryDetailModel.getCountryById(id)

    fun stopShowing() {
        countryDetailModel.stopCoroutine()
    }

    private fun loadData() {
        doWork {
            val result = countryDetailModel.loadData()
            Log.d("CountrDetViewMod", result.result)
        }
    }
}
