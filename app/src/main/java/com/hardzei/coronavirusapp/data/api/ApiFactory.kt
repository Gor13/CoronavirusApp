package com.hardzei.coronavirusapp.data.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL_FOR_STATISTIC =
        "https://api.covid19api.com/"
    private const val BASE_URL_FOR_IMAGES =
        "https://www.flickr.com/services/"

    private fun <T> createService(baseUrl: String, typeParameterClass: Class<T>): T {
        val retrofitCoronaStatistic = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
        return retrofitCoronaStatistic.create(typeParameterClass)
    }

    val apiServiceForCoronastatistic = createService(BASE_URL_FOR_STATISTIC, ApiService::class.java)
    val apiServiceForCountryImages = createService(BASE_URL_FOR_IMAGES, ApiService::class.java)
}