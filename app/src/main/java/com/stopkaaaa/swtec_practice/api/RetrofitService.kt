package com.stopkaaaa.swtec_practice.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.WeatherForecast

private const val API_KEY = "4b7a044e1a4df3a657a78c9725be33ac"

interface RetrofitService {

    @GET("data/2.5/onecall")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double = 56.302947,
        @Query("lon") longitude: Double = 44.021527,
        @Query("exclude") exclude: String = arrayOf(
            "current",
            "minutely",
            "hourly",
            "alerts"
        ).joinToString(separator = ","),
        @Query("units") units: String = "metric",
        @Query("appId") apiKey: String = API_KEY
    ): WeatherForecast

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherForecast(
        @Query("q") place: String = arrayOf(
            "Nizhniy Novgorod", "RUS"
        ).joinToString(separator = ","),
        @Query("appId") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherForecast

    @GET
    fun getWeatherImage(@Url imageUrl: String): Call<ResponseBody>
}