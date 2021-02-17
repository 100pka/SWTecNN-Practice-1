package com.stopkaaaa.swtec_practice.api

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import java.lang.Exception

class CurrentWeatherForecastLoader(context: Context) : AsyncTaskLoader<CurrentWeatherForecast>(context) {

    var currentWeather : CurrentWeatherForecast? = null

    override fun loadInBackground(): CurrentWeatherForecast? {
        try {
            currentWeather = RetrofitClient.getCurrentWeather().execute().body()
        } catch (e: Exception) {
            Log.d("Retrofit onFailure: ", e.message.toString())
        }
        return currentWeather
    }

    override fun onStartLoading() {
        if (currentWeather != null) {
            deliverResult(currentWeather)
        }
        if (currentWeather == null || takeContentChanged()) {
            forceLoad()
        }
    }
}