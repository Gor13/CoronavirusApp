package com.hardzei.coronavirusapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.Photo
import com.hardzei.coronavirusapp.data.repository.BaseRepository
import com.hardzei.coronavirusapp.data.repository.Repository
import kotlinx.coroutines.*


class CountryDetailModel(private val repository: Repository)
    : BaseModel<CountryDetailModel.Params, CountryDetailModel.Result>() {


    companion object {
        private const val CONST_URL = "https://www.flickr.com/services/"
    }

    var onDetailCountryChangeListener: OnDetailCountryChangeListener? = null
    private var country: Country? = null
    private var counter = 0
    private var b: Job? = null

    init {
        repository.onDetailCountryChangeListener =
                object : BaseRepository
                .OnDetailCountryChangeListener {
                    override fun onGetCountrySuccess(country: Country) {
                        this@CountryDetailModel.country = country
                        onDetailCountryChangeListener?.onGetCountrySuccess(country)
                    }
                    override fun onError(errors: String) {
                        onDetailCountryChangeListener?.onError(errors)
                    }
                }

    }

    fun stopCoroutine() {
        b?.cancel()
        b = null
    }

    fun getCountryById(id: Int) = repository.getCountryById(id)

    private fun setImage(url: String) {
        onDetailCountryChangeListener?.onGetLinkSuccess(url)
    }

    private suspend fun getImageUrl(listWithLinks: List<Photo>?) {

        var str: String

        listWithLinks?.let {
            with(listWithLinks[counter]) {
                str = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
                if (counter != listWithLinks.size - 1)
                    counter++
                else
                    counter = 0
            }
            Log.d("TEST-setImage", "success")

            try {
                withContext(Dispatchers.Main) {
                    setImage(str)
                }
            } catch (ex: Exception) {
                b?.cancel()
                counter = 0
                Log.d("ERROR-0001", "Exit from coroutine loop: ${ex.message}")
            }
        }
    }

    class Params()
    data class Result(val result: String)

    override suspend fun LoadData(): Result {

        val result = "Success"
        val listWithLinks = repository
                .loadListWithLinksOfImages(Repository.Params("${country?.country}+capital", CONST_URL))
                .links?.photos?.photo


        Log.d("TEST_CDModel", listWithLinks.toString())

        if (b == null) {
            b = CoroutineScope(Dispatchers.IO).launch {
                val maxSize = 4
                while (true) {
                    listWithLinks.let {
                        getImageUrl(it?.subList(0, maxSize))
                        delay(5000)
                    }
                }
            }
        }
        return Result(result)
    }
}