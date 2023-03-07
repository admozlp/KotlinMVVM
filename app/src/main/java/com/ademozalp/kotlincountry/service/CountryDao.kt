package com.ademozalp.kotlincountry.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ademozalp.kotlincountry.model.Country


@Dao
interface CountryDao {
    @Insert
    suspend fun insertAll(vararg countries : Country) : List<Long>// kaydedilenlerin uuid doner

    @Query("SELECT * FROM country")
    suspend fun getAllCountries()  : List<Country>

    @Query(value = "SELECT * FROM country WHERE uuid = :uuid")
    suspend  fun getCountry(uuid: Int) : Country

    @Query(value = "DELETE FROM country")
    suspend fun deleteAllCountries()

}