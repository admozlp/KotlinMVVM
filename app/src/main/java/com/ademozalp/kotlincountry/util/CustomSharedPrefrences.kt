package com.ademozalp.kotlincountry.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPrefrences {

    companion object{
        private const val PREFERENCES_TIME = "preferences_time"
        private var sharedPrefrences : SharedPreferences? = null
        @Volatile private var instance : CustomSharedPrefrences? = null

        private val lock = Any()

        operator fun invoke(context : Context) : CustomSharedPrefrences = instance ?: synchronized(lock){
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context:Context) : CustomSharedPrefrences{
            sharedPrefrences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPrefrences()
        }
    }

    fun saveTime(time : Long){
        sharedPrefrences?.edit(commit = true){
            putLong(PREFERENCES_TIME, time)
        }
    }
    fun getTime() = sharedPrefrences?.getLong(PREFERENCES_TIME, 0)
}