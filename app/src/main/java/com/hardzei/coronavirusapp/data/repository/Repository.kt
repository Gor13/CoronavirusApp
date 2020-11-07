package com.hardzei.coronavirusapp.data.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hardzei.coronavirusapp.data.api.ApiService
import com.hardzei.coronavirusapp.data.database.CountryDao
import com.hardzei.coronavirusapp.data.entity.coronastatistic.CountriesRequest
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.ImageOfCountryRequest
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Repository(private val countryDao: CountryDao) : BaseRepository<Repository.Params, Repository.Result>() {

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
    }

    private fun deleteGlobal() = CoroutineScope(Dispatchers.IO).launch {
        countryDao.deleteGlobal()
    }

    override suspend fun loadDataWithCountriesStatistic(params: Params): Result {
        var status = "Faild"
        var response: Response<CountriesRequest>? = null
        val retrofitCountries = Retrofit
                .Builder()
                .baseUrl(params.url)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ApiService::class.java)
        try {
            response = retrofitCountries
                    .getCountriesFromJson()
                    .await()

            Log.d("TEST_loadata_countr2", response.body().toString() + "-" + params.url)

            status = "Success"

            getSortedCountries("id ASC") // get sorted statistic from DB after fill the DB

        } catch (ex: Exception) {
            Log.d("TEST_loadata_countr1", ex.message.toString())
        }

        when (status) {
            "Success" -> {
                deleteAll()
                deleteGlobal()
                response?.body()?.let {
                    insertAll(it.countries)
                    insertGlobal(it.global)
                }
            }
            else -> status = "Faild"
        }

        Log.d("TEST_loadata_countr3", status)

        return Result(null, status)
    }

    class Params(val request: String, val url: String)
    data class Result(val links: ImageOfCountryRequest?, val status: String)

    override suspend fun loadListWithLinksOfImages(params: Params): Result {
        var result: Response<ImageOfCountryRequest>? = null
        var status = "Faild"
        val retrofitLinks = Retrofit
                .Builder()
                .baseUrl(params.url)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ApiService::class.java)
        try {
            result = retrofitLinks
                    .getCountriesImages(text = params.request)
                    .await()
            status = "Success"
        } catch (ex: Exception) {
            Log.d("TEST_loadata_links1", ex.message.toString())
        }

        Log.d("TEST_loadata_links2", result?.body().toString())

        Log.d("TEST_loadata_links3", status)
        return Result(result?.body(), status)
    }
}