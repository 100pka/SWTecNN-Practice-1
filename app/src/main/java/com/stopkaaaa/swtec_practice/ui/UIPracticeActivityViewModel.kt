package com.stopkaaaa.swtec_practice.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopkaaaa.swtec_practice.api.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.Exception

class UIPracticeActivityViewModel : ViewModel() {

    private val _mutableCurrentWeather = MutableLiveData<CurrentWeatherForecast>()
    val currentWeather: LiveData<CurrentWeatherForecast> = _mutableCurrentWeather

    private val _mutableDailyForecastList = MutableLiveData<List<DailyForecast>>()
    val dailyForecastList: LiveData<List<DailyForecast>> = _mutableDailyForecastList

    private val _mutableCurrentWeatherLoadingState = MutableLiveData(CurrentWeatherLoadingState.LOADING)
    val currentWeatherLoadingState: LiveData<CurrentWeatherLoadingState> = _mutableCurrentWeatherLoadingState

    private val _mutableDailyWeatherLoadingState = MutableLiveData(DailyWeatherLoadingState.LOADING)
    val dailyWeatherLoadingState: LiveData<DailyWeatherLoadingState> = _mutableDailyWeatherLoadingState

    init {

        viewModelScope.launch {
            _mutableCurrentWeatherLoadingState.value = CurrentWeatherLoadingState.LOADING
            _mutableDailyWeatherLoadingState.value = DailyWeatherLoadingState.LOADING

            val current = try {
                RetrofitClient.getCurrentWeather()
            } catch (e: Exception) {
                _mutableCurrentWeatherLoadingState.value = CurrentWeatherLoadingState.ERROR
                null
            }

            val daily = try {
                RetrofitClient.getWeatherForecast().daily
            } catch (e: Exception) {
                _mutableDailyWeatherLoadingState.value = DailyWeatherLoadingState.ERROR
                null
            }

            current?.let {
                _mutableCurrentWeather.value = it
                _mutableCurrentWeatherLoadingState.value = CurrentWeatherLoadingState.DONE
            }

            daily?.let {
                _mutableDailyForecastList.value = it.subList(0, 5)
                _mutableDailyWeatherLoadingState.value = DailyWeatherLoadingState.DONE
            }
        }
    }
}