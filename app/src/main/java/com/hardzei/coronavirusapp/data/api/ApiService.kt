package com.hardzei.coronavirusapp.data.api

import com.hardzei.coronavirusapp.data.entity.coronastatistic.CountriesRequest
import com.hardzei.coronavirusapp.data.entity.imagesofcountries.ImageOfCountryRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("summary")
    fun getCountriesFromJson(): Deferred<Response<CountriesRequest>>

    @GET("rest")
    fun getCountriesImages(
        @Query(QUERY_PARAM_METHOD) method: String = "flickr.photos.search",
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "25d76ba213361b32e3c2925caa77cd43",
        @Query(QUERY_PARAM_TAG_MODE) tagMode: String = "AND",
        @Query(QUERY_PARAM_TEXT) text: String = "belarus+capital",
        @Query(QUERY_PARAM_SORT) sort: String = "interestingness-desc",
        @Query(QUERY_PARAM_FORMAT) format: String = "json",
        @Query(QUERY_PARAM_NOJSONCALLBACK) nojsoncallback: String = "1",
    ): Deferred<Response<ImageOfCountryRequest>>

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_METHOD = "method"
        private const val QUERY_PARAM_TAG_MODE = "tag_mode"
        private const val QUERY_PARAM_SORT = "sort"
        private const val QUERY_PARAM_FORMAT = "format"
        private const val QUERY_PARAM_NOJSONCALLBACK = "nojsoncallback"
        private const val QUERY_PARAM_TEXT = "text"
    }
}
