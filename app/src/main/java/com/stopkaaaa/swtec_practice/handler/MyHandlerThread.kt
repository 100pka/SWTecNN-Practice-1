package com.stopkaaaa.swtec_practice.handler

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import java.lang.Exception
import java.lang.ref.WeakReference

class MyHandlerThread(name: String) : HandlerThread(name, android.os.Process.THREAD_PRIORITY_BACKGROUND){

    lateinit var handler: Handler
    // use weak reference to avoid activity being leaked
    var callback: WeakReference<BindResultCallback>? = null

    // Used by UI thread to send a runnable to the worker thread's message queue
    fun getWeatherData(){
        handler = Handler(looper)
        handler.post{
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
            if (!isInterrupted && callback != null && callback?.get() != null) {
                if (currentWeather != null) {
                    callback?.get()?.bindResult(currentWeather, dailyWeather.subList(0, 5))
                }
            }
        }
    }

    // The UiThreadCallback is used to send message to UI thread
    fun setBindResultCallback(callback: BindResultCallback){
        this.callback = WeakReference<BindResultCallback>(callback)
    }

}