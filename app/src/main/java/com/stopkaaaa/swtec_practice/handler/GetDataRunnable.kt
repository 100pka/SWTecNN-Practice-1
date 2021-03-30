package com.stopkaaaa.swtec_practice.handler

import android.util.Log
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.Exception
import java.lang.ref.WeakReference

class GetDataRunnable() : Runnable {

    private var callback: WeakReference<BindResultCallback>? = null

    override fun run() {
        var currentWeather: CurrentWeatherForecast? = null
        val dailyWeather: MutableList<DailyForecast> = mutableListOf()

        try {
            currentWeather = RetrofitClient.getCurrentWeather().execute().body()
            RetrofitClient.getWeatherForecast().execute().body()?.daily?.let {
                dailyWeather.clear()
                dailyWeather.addAll(
                    it
                )
            }
        } catch (e: Exception) {
            Log.d("Retrofit onFailure: ", e.message.toString())
        }
        if (!Thread.currentThread().isInterrupted && callback != null && callback?.get() != null) {
            if (currentWeather != null) {
                callback?.get()?.bindResult(currentWeather, dailyWeather.subList(0, 5))
            }
        }
    }

    fun setBindResultCallback(callback: BindResultCallback){
        this.callback = WeakReference<BindResultCallback>(callback)
    }
}