package com.hardzei.coronavirusapp.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    private const val BASE_URL_FOR_STATISTIC =
        "https://api.covid19api.com/"
    private const val BASE_URL_FOR_IMAGES =
        "https://www.flickr.com/services/"

    private fun <T> createService(baseUrl: String, typeParameterClass: Class<T>): T {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(typeParameterClass)
    }

    val apiServiceForCoronastatistic = createService(BASE_URL_FOR_STATISTIC, ApiService::class.java)
    val apiServiceForCountryImages = createService(BASE_URL_FOR_IMAGES, ApiService::class.java)
}
