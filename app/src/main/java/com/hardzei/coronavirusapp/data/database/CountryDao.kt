package com.hardzei.coronavirusapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Country
import com.hardzei.coronavirusapp.data.entity.coronastatistic.Global

@Dao
interface CountryDao {
    @RawQuery(observedEntities = arrayOf(Country::class))
    suspend fun getAllCountrisSortedBy(query: SupportSQLiteQuery): List<Country>

    @Query("SELECT * FROM country_table WHERE id=:id ")
    suspend fun getCountryById(id: Int): Country

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCountries(countries: List<Country>)

    @Query("DELETE FROM country_table")
    suspend fun deleteAllCountries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlobal(global: Global)

    @Query("DELETE FROM global_table")
    suspend fun deleteGlobal()

    @Query("SELECT * FROM global_table ORDER BY totalConfirmed")
    suspend fun getGlobal(): List<Global>
}