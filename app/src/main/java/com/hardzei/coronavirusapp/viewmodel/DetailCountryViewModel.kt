package com.hardzei.coronavirusapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.model.CountryDetailModel
import io.reactivex.disposables.CompositeDisposable

class DetailCountryViewModel(private val countryDetailModel: CountryDetailModel) : ViewModel() {
    private var compositeDisposable = CompositeDisposable()

    private val _countryDetail: MutableLiveData<Country> = MutableLiveData()
    val countryDetail: LiveData<Country>
        get() = _countryDetail

    private val _countryLink = MutableLiveData<String>()
    val countryLink: LiveData<String>
        get() = _countryLink

    private val _errors: MutableLiveData<String> = MutableLiveData()
    val errors: LiveData<String>
        get() = _errors

    init {
        countryDetailModel
            .onDetailCountryChangeListener = object : CountryDetailModel
        .OnDetailCountryChangeListener {
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
        compositeDisposable.addAll(countryDetailModel.loadData())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}