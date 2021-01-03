package com.hardzei.coronavirusapp.koin

import android.app.Application
import androidx.room.Room
import com.hardzei.coronavirusapp.data.api.ApiFactory
import com.hardzei.coronavirusapp.data.database.CountryDao
import com.hardzei.coronavirusapp.data.database.CountryDatabase
import com.hardzei.coronavirusapp.data.repository.Repository
import com.hardzei.coronavirusapp.model.CountriesModel
import com.hardzei.coronavirusapp.model.CountryDetailModel
import com.hardzei.coronavirusapp.viewmodel.CountriesListViewModel
import com.hardzei.coronavirusapp.viewmodel.DetailCountryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val retrofitModule = module {
    single { ApiFactory }
}

val databaseModule = module {

    fun provideDatabase(application: Application): CountryDatabase {
        return Room.databaseBuilder(application, CountryDatabase::class.java, "countrydatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCountriesDao(database: CountryDatabase): CountryDao {
        return database.countryDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}

val repositoryModule = module {

    fun provideRepository(dao: CountryDao, apiFactory: ApiFactory): Repository {
        return Repository(dao, apiFactory)
    }
    factory { provideRepository(get(), get()) }
}

val detailCountryViewModelModule = module {
    viewModel {
        DetailCountryViewModel(countryDetailModel = get())
    }
}

val countriesListViewModelModule = module {

    viewModel {
        CountriesListViewModel(countriesModel = get())
    }
}

val countriesModelModule = module {
    single { CountriesModel(repository = get()) }
}

val countryDetailModelModule = module {
    single { CountryDetailModel(repository = get()) }
}
