package com.ademozalp.kotlincountry.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ademozalp.kotlincountry.model.Country


@Database(entities = [Country::class], version = 1)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao() : CountryDao

    companion object{
        @Volatile private var instance : CountryDatabase? = null

        private var lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        ).build()
    }
}