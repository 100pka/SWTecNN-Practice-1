package com.stopkaaaa.swtec_practice.ui

import android.util.Log
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.ref.WeakReference

class MyThread : Thread() {

    private var uiThreadCallbackWeakReference: WeakReference<UiThreadCallback>? = null

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
            if (isInterrupted) return

            if (uiThreadCallbackWeakReference != null && uiThreadCallbackWeakReference!!.get() != null) {
                uiThreadCallbackWeakReference!!.get()?.showMessageToast("Something went wrong: " + e.message)
            }
        }

        if (isInterrupted) return
        if (uiThreadCallbackWeakReference != null && uiThreadCallbackWeakReference!!.get() != null &&
                currentWeather != null && dailyWeather.isNotEmpty()) {
            uiThreadCallbackWeakReference!!.get()?.setCurrentWeather(currentWeather)
            uiThreadCallbackWeakReference!!.get()?.setDailyForecastList(dailyWeather.subList(0, 5))
        }
    }

    fun setUiThreadCallback(uiThreadCallback: UiThreadCallback) {
        uiThreadCallbackWeakReference = WeakReference(uiThreadCallback)
    }
}
