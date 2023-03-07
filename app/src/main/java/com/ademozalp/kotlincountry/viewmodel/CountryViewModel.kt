package com.ademozalp.kotlincountry.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ademozalp.kotlincountry.model.Country
import com.ademozalp.kotlincountry.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application) {
    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid : Int){
        launch {
            val country =  CountryDatabase(getApplication()).countryDao().getCountry(uuid)
            countryLiveData.value = country
        }
    }

}