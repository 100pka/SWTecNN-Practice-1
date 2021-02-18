package com.stopkaaaa.swtec_practice.threadpool

import smart.sprinkler.app.api.model.CurrentWeather
import smart.sprinkler.app.api.model.CurrentWeatherForecast

interface UiThreadCallback {
    fun bindResult(currentWeather: CurrentWeatherForecast)
}