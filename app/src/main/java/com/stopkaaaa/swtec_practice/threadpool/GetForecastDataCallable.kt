package com.stopkaaaa.swtec_practice.threadpool

import android.util.Log
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import java.lang.ref.WeakReference
import java.util.concurrent.Callable

class GetForecastDataCallable : Callable<Unit> {

    var threadPoolReference: WeakReference<ThreadPoolManager>? = null

    override fun call() {
        var currentWeather: CurrentWeatherForecast? = null
        try {
            currentWeather = RetrofitClient.getCurrentWeather().execute().body()
        } catch (e: Exception) {
            Log.d("Retrofit onFailure: ", e.message.toString())
        }
        if (threadPoolReference != null && threadPoolReference?.get() != null && currentWeather != null) {
            threadPoolReference?.get()?.postResultToUi(currentWeather)
        }
    }

    fun setThreadPoolManager(threadPoolManager: ThreadPoolManager) {
        threadPoolReference = WeakReference<ThreadPoolManager>(threadPoolManager)
    }
}