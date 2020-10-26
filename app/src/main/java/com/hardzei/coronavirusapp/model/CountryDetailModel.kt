package com.hardzei.coronavirusapp.model

import android.util.Log
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.Photo
import com.hardzei.coronavirusapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryDetailModel(private val repository: Repository) {

    var onDetailCountryChangeListener: OnDetailCountryChangeListener? = null
    private var country: Country? = null
    private var counter = 0
    private var b: Job? = null

    init {
        repository.onDetailCountryChangeListener =
            object : Repository
            .OnDetailCountryChangeListener {
                override fun onGetCountrySuccess(country: Country) {
                    this@CountryDetailModel.country = country
                    onDetailCountryChangeListener?.onGetCountrySuccess(country)
                }

                override fun onLoadLinksSuccess(links: List<Photo>?) {
                    Log.d("TEST_CDModel", links.toString())
                    if (b == null) {
                        b = CoroutineScope(Dispatchers.IO).launch {
                            while (true) {
                                getImageUrl(links)
                                delay(5000)
                            }
                        }
                    }
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


    fun loadData() = repository
        .loadDataWithImagesOfCountry("${country?.country}+capital")

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

    interface OnDetailCountryChangeListener {
        fun onGetCountrySuccess(
            country: Country
        )

        fun onGetLinkSuccess(
            link: String
        )

        fun onError(errors: String)
    }
}