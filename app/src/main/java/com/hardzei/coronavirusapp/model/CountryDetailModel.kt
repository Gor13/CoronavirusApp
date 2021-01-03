package com.hardzei.coronavirusapp.model

import android.util.Log
import com.hardzei.coronavirusapp.ERROR_STATUS
import com.hardzei.coronavirusapp.SUCCESS_STATUS
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.Photo
import com.hardzei.coronavirusapp.data.repository.BaseRepository
import com.hardzei.coronavirusapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryDetailModel(private val repository: Repository) :
    BaseModel<CountryDetailModel.Params, CountryDetailModel.Result> {

    var onDetailCountryChangeListener: BaseModel.OnDetailCountryChangeListener? = null
    private var country: Country? = null
    private var counter = 0
    private var job: Job? = null

    init {
        repository.onDetailCountryChangeListener =
            object : BaseRepository.OnDetailCountryChangeListener {
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
        job?.cancel()
        job = null
    }

    fun getCountryById(id: Int) = repository.getCountryById(id)

    private fun setImage(url: String) {
        onDetailCountryChangeListener?.onGetLinkSuccess(url)
    }

    private suspend fun getImageUrl(listWithLinks: List<Photo>?) {

        var str: String

        try {
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
                    job?.cancel()
                    counter = 0

                    Log.d("ERROR-0001", "Exit from coroutine loop: ${ex.message}")
                }
            }
        } catch (ex: Exception) {
            job?.cancel()
            counter = 0

            Log.d("ERROR-0002", "Exit from coroutine loop: ${ex.message}")
        }
    }

    class Params
    data class Result(val result: String)

    override suspend fun loadData(): Result {

        val listWithLinks = repository
            .loadListWithLinksOfImages(Repository.Params("${country?.country}+capital"))
            .links

        Log.d("TEST_CDModel", listWithLinks.toString())

        if (job == null) {
            job = CoroutineScope(Dispatchers.IO).launch {
                var maxSize: Int
                while (true) {
                    listWithLinks.let {

                        val sizeListWithLinks = it?.size

                        when (sizeListWithLinks) {
                            in MIN_SIZE_OF_LIST..MAX_SIZE_OF_LIST ->
                                maxSize =
                                    sizeListWithLinks ?: 0
                            else -> maxSize = MAX_SIZE_OF_LIST + 1
                        }
                        @Suppress("UNCHECKED_CAST")
                        getImageUrl(it?.subList(0, maxSize) as List<Photo>?)
                        delay(DELAY_FOR_CHANGE_PICTURES)
                    }
                }
            }
        }

        return Result(if (listWithLinks != null) SUCCESS_STATUS else ERROR_STATUS)
    }

    private companion object {
        private const val MIN_SIZE_OF_LIST = 0
        private const val MAX_SIZE_OF_LIST = 3
        private const val DELAY_FOR_CHANGE_PICTURES = 5000L
    }
}
