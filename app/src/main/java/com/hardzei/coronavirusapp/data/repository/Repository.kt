package com.hardzei.coronavirusapp.data.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hardzei.coronavirusapp.data.api.ApiFactory
import com.hardzei.coronavirusapp.data.database.CountryDao
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.Photo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val countryDao: CountryDao) {

    var onStatisticChangeListener: OnStatisticChangeListener? = null
    var onDetailCountryChangeListener: OnDetailCountryChangeListener? = null

    fun getSortedCountries(sortedBy: String) =
        CoroutineScope(Dispatchers.IO).launch {

            onStatisticChangeListener?.onSuccess(
                countryDao
                    .getAllCountrisSortedBy(
                        SimpleSQLiteQuery(
                            "SELECT * FROM country_table ORDER BY $sortedBy"
                        )
                    ),
                countryDao.getGlobal()
            )
        }

    fun getCountryById(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        onDetailCountryChangeListener?.onGetCountrySuccess(countryDao.getCountryById(id))
    }

    private fun insertAll(countries: List<Country>) = CoroutineScope(Dispatchers.IO).launch {
        countryDao.insertAllCountries(countries)
    }

    private fun deleteAll() = CoroutineScope(Dispatchers.IO).launch {
        countryDao.deleteAllCountries()
    }

    private fun insertGlobal(global: Global) = CoroutineScope(Dispatchers.IO).launch {
        countryDao.insertGlobal(global)
        getSortedCountries("id ASC")
    }

    private fun deleteGlobal() = CoroutineScope(Dispatchers.IO).launch {
        countryDao.deleteGlobal()
    }

    fun loadDataWithCountriesStatistic() = ApiFactory
        .apiServiceForCoronastatistic
        .getCountriesFromJson()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Log.d("TEST_Rep_load_sec", it.countries.toString())
            deleteAll()
            deleteGlobal()
            insertAll(it.countries)
            insertGlobal(it.global)
            onStatisticChangeListener?.onError("success")
        }, {
            Log.d("TEST_Rep ERROR-1000", it.toString())
            onStatisticChangeListener?.onError("${it.message}")
        })

    fun loadDataWithImagesOfCountry(requestToSearch: String) = ApiFactory
        .apiServiceForCountryImages
        .getCountriesImages(text = requestToSearch)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({

            Log.d("TEST_Rep_load_imags", it.toString())

            val maxSize: Int
            val currentSize = it.photos?.photo?.size ?: 0
            when (currentSize) {
                in -1..0 -> throw Exception("We don't have images with this country :-(")
                in 1..3 -> maxSize = currentSize
                else -> maxSize = 4
            }

            onDetailCountryChangeListener?.onLoadLinksSuccess(it.photos?.photo?.subList(0, maxSize))
        }, {

            Log.d("TEST_Rep ERROR-2000", it.toString())

            onDetailCountryChangeListener?.onError("${it.message}")
        })

    interface OnStatisticChangeListener {
        fun onSuccess(
            allCountries: List<Country>,
            global: List<Global>
        )

        fun onError(errors: String)
    }

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
            country: Country
        )

        fun onLoadLinksSuccess(
            links: List<Photo>?
        )

        fun onError(errors: String)
    }
}