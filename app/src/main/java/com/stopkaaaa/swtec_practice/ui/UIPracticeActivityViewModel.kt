package com.stopkaaaa.swtec_practice.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import smart.sprinkler.app.api.model.WeatherForecast

class UIPracticeActivityViewModel : ViewModel() {

    private val _mutableCurrentWeather = MutableLiveData<CurrentWeatherForecast>()
    val currentWeather: LiveData<CurrentWeatherForecast> get() = _mutableCurrentWeather

    private val _mutableDailyForecastList = MutableLiveData<List<DailyForecast>>()
    val dailyForecastList: LiveData<List<DailyForecast>> get() = _mutableDailyForecastList

    private val compositeDisposable = CompositeDisposable()

    init {

        compositeDisposable.add(RetrofitClient.getCurrentWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<CurrentWeatherForecast>() {
                override fun onSuccess(t: CurrentWeatherForecast?) {
                    _mutableCurrentWeather.value = t
                }

                override fun onError(e: Throwable?) {
                    Log.d("ViewModel: ", "Something went wrong")
                }

            }))

        compositeDisposable.add(RetrofitClient.getWeatherForecast()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<WeatherForecast>() {
            override fun onSuccess(t: WeatherForecast?) {
               _mutableDailyForecastList.value = t?.daily?.subList(0, 5)
            }

            override fun onError(e: Throwable?) {
                Log.d("ViewModel: ", "Something went wrong")
            }

        }))

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}