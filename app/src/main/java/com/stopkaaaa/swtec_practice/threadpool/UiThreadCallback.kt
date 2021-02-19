package com.stopkaaaa.swtec_practice.threadpool

import smart.sprinkler.app.api.model.CurrentWeather
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast

interface UiThreadCallback {

    fun bindCurrentWeatherToUi(currentWeather: CurrentWeatherForecast)

    fun bindDailyWeatherToUi(dailyWeather: List<DailyForecast>)

}