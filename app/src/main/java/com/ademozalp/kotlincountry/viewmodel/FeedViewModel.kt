package com.ademozalp.kotlincountry.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ademozalp.kotlincountry.model.Country
import com.ademozalp.kotlincountry.service.CountryAPIService
import com.ademozalp.kotlincountry.service.CountryDatabase
import com.ademozalp.kotlincountry.util.CustomSharedPrefrences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application : Application) : BaseViewModel(application) {
    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private val sharedPrefrences = CustomSharedPrefrences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()
    val errorText = MutableLiveData<String>()

    fun refreshData() {
        val updateTime = sharedPrefrences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQL()
        }else{
            getDataFromApi()
        }
    }

    fun refreshFromApi(){
        getDataFromApi()
    }

    private fun getDataFromSQL() {
        launch {
            countryLoading.value = true
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
        }
    }

    private fun getDataFromApi() {
        countryLoading.value = true
        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        insertToRoom(t)
                    }
                    override fun onError(e: Throwable) {
                        countryError.value = true
                        countryLoading.value = false
                        errorText.value = e.localizedMessage
                    }
                })
        )
    }

    private fun showCountries(countryList : List<Country>){
        countries.value = countryList
        countryLoading.value = false
        countryError.value = false
    }

    private fun insertToRoom(list : List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val longList = dao.insertAll(*list.toTypedArray())
            var i = 0
            while(i < list.size){
                list[i].uuid = longList[i].toInt()
                i++
            }
            showCountries(list)
        }
        sharedPrefrences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}