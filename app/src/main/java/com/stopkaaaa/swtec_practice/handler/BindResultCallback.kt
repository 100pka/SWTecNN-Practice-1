package com.stopkaaaa.swtec_practice.handler

import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast

interface BindResultCallback {
    fun bindResult(currentWeatherForecast: CurrentWeatherForecast, dailyWeather: List<DailyForecast>)
}