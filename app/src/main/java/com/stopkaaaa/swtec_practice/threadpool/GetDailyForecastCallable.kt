package com.stopkaaaa.swtec_practice.threadpool

import android.util.Log
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.ref.WeakReference
import java.util.concurrent.Callable

class GetDailyForecastCallable : Callable<Unit> {
    var threadPoolReference: WeakReference<ThreadPoolManager>? = null

    override fun call() {
        val dailyWeather: MutableList<DailyForecast> = mutableListOf()
        try {
            RetrofitClient.getWeatherForecast().execute().body()?.daily?.let {
                dailyWeather.clear()
                dailyWeather.addAll(
                    it
                )
            }
        } catch (e: Exception) {
            Log.d("Retrofit onFailure: ", e.message.toString())
        }
        if (threadPoolReference != null && threadPoolReference?.get() != null && dailyWeather.isNotEmpty()) {
            threadPoolReference?.get()?.postDailyWeatherToUi(dailyWeather.subList(0, 5))
        }
    }

    fun setThreadPoolManager(threadPoolManager: ThreadPoolManager) {
        threadPoolReference = WeakReference<ThreadPoolManager>(threadPoolManager)
    }
}