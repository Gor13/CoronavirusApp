package com.hardzei.coronavirusapp

import android.app.Application
import com.hardzei.coronavirusapp.koin.countriesListViewModelModule
import com.hardzei.coronavirusapp.koin.countriesModelModule
import com.hardzei.coronavirusapp.koin.countryDetailModelModule
import com.hardzei.coronavirusapp.koin.databaseModule
import com.hardzei.coronavirusapp.koin.detailCountryViewModelModule
import com.hardzei.coronavirusapp.koin.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoronaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CoronaApplication)
            modules(
                databaseModule,
                repositoryModule,
                countriesListViewModelModule,
                detailCountryViewModelModule,
                countriesModelModule,
                countryDetailModelModule
            )
        }
    }
}