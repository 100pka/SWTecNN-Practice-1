package com.stopkaaaa.swtec_practice.ui

import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast

interface UiThreadCallback {
    fun showMessageToast(message: String)

    fun setDailyForecastList(dailyForecastList: List<DailyForecast>)

    fun setCurrentWeather(currentWeatherForecast: CurrentWeatherForecast)
}