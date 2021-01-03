package com.hardzei.coronavirusapp.data.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.hardzei.coronavirusapp.ERROR_STATUS
import com.hardzei.coronavirusapp.SUCCESS_STATUS
import com.hardzei.coronavirusapp.UPDATED_STATUS
import com.hardzei.coronavirusapp.data.api.ApiFactory
import com.hardzei.coronavirusapp.data.database.CountryDao
import com.hardzei.coronavirusapp.data.entity.coronastatistic.CountriesRequest
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.ImageOfCountryRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

class Repository(private val countryDao: CountryDao, private val apiFactory: ApiFactory) :
    BaseRepository<Repository.Params, Repository.Result> {

    var onStatisticChangeListener: BaseRepository.OnStatisticChangeListener? = null
    var onDetailCountryChangeListener: BaseRepository.OnDetailCountryChangeListener? = null
    private val mutex = Mutex()

    fun getSortedCountries(sortedBy: String) = CoroutineScope(Dispatchers.IO).launch {
        mutex.withLock {
            getAllCountries(sortedBy)
            getGlobal()
        }
    }

    fun getCountryById(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        onDetailCountryChangeListener?.onGetCountrySuccess(countryDao.getCountryById(id))
    }

    private suspend fun getAllCountries(request: String) = CoroutineScope(Dispatchers.IO).launch {
        var countries = countryDao
            .getAllCountrisSortedBy(
                SimpleSQLiteQuery(
                    "SELECT * FROM country_table ORDER BY $request"
                )
            )
        while (countries.isEmpty()) {
            delay(DELAY_FOR_DB)
            countries = countryDao
                .getAllCountrisSortedBy(
                    SimpleSQLiteQuery(
                        "SELECT * FROM country_table ORDER BY $request"
                    )
                )
            Log.d("REPOS", "gALL $countries")
        }
        Log.d("REPOS", "gALL $countries")
        onStatisticChangeListener?.onSuccessWithList(
            countries
        )
        onStatisticChangeListener?.onError(SUCCESS_STATUS)
    }

    private suspend fun getGlobal() = CoroutineScope(Dispatchers.IO).launch {
        var global = countryDao.getGlobal()
        while (global.isEmpty()) {
            delay(DELAY_FOR_DB)
            global = countryDao.getGlobal()
            Log.d("REPOS", "gGlob $global")
        }
        Log.d("REPOS", "gGlob $global")
        onStatisticChangeListener?.onSuccessWithGlobal(
            global
        )
        onStatisticChangeListener?.onError(SUCCESS_STATUS)
    }

    private suspend fun updateDB(countries: List<Country>, global: Global) {
        deleteAll()
        deleteGlobal()
        insertAll(countries)
        insertGlobal(global)
    }

    private suspend fun insertAll(countries: List<Country>) =
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                countryDao.insertAllCountries(countries)
                Log.d("REPOS", "iAll")
            }
        }

    private suspend fun deleteAll() =
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                countryDao.deleteAllCountries()
                Log.d("REPOS", "dAll")
            }
        }

    private suspend fun insertGlobal(global: Global) =
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                countryDao.insertGlobal(global)
                Log.d("REPOS", "iGlob")
            }
        }

    private suspend fun deleteGlobal() =
        CoroutineScope(Dispatchers.IO).launch {
            mutex.withLock {
                countryDao.deleteGlobal()

                Log.d("REPOS", "dGlob")
            }
        }

    override suspend fun loadDataWithCountriesStatistic(params: Params): Result {
        var response: Response<CountriesRequest>? = null
        var status: String
        try {
            response = apiFactory
                .apiServiceForCoronastatistic
                .getCountriesFromJson()
                .await()

            Log.d("TEST_loadata_countr2", response.body().toString())

            onStatisticChangeListener?.onError(UPDATED_STATUS)
            status = SUCCESS_STATUS
        } catch (ex: Exception) {

            Log.d("TEST_loadata_countr1", ex.message.toString())

            onStatisticChangeListener?.onError(ex.message.toString())
            status = ERROR_STATUS
        }

        response?.body()?.let {
            updateDB(it.countries, it.global)
        }
        return Result(null, status)
    }

    class Params(val request: String)
    data class Result(val links: List<Any>?, val status: String)

    override suspend fun loadListWithLinksOfImages(params: Params): Result {
        var result: Response<ImageOfCountryRequest>? = null
        var status: String
        try {
            result = apiFactory
                .apiServiceForCountryImages
                .getCountriesImages(text = params.request)
                .await()
            onDetailCountryChangeListener?.onError(SUCCESS_STATUS)
            status = SUCCESS_STATUS
        } catch (ex: Exception) {

            Log.d("TEST_loadata_links1", ex.message.toString())

            onDetailCountryChangeListener?.onError(ex.message.toString())
            status = ERROR_STATUS
        }

        Log.d("TEST_loadata_links2", result?.body().toString())

        return Result(result?.body()?.photos?.photo, status)
    }

    private companion object {
        const val DELAY_FOR_DB = 50L
    }
}
