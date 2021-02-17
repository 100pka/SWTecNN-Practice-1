package com.stopkaaaa.swtec_practice.api

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.WeatherForecast
import java.lang.Exception

class WeatherForecastLoader(context: Context) : AsyncTaskLoader<WeatherForecast>(context)  {
    var currentWeather : WeatherForecast? = null

    override fun loadInBackground(): WeatherForecast? {
        try {
            currentWeather = RetrofitClient.getWeatherForecast().execute().body()
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