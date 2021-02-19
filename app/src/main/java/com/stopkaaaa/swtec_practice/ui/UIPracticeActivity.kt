package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding
import com.stopkaaaa.swtec_practice.threadpool.GetDailyForecastCallable
import com.stopkaaaa.swtec_practice.threadpool.GetForecastDataCallable
import com.stopkaaaa.swtec_practice.threadpool.ThreadPoolManager
import com.stopkaaaa.swtec_practice.threadpool.UiThreadCallback
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast

class UIPracticeActivity : AppCompatActivity(), UiThreadCallback {

    private lateinit var binding: ActivityUIPracticeBinding
    private val locationAdapter = LocationAdapter()
    private val whetherAdapter = WhetherAdapter()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUIPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> binding.horizontalGuideline.setGuidelinePercent(
                0.50f
            )
            else -> binding.horizontalGuideline.setGuidelinePercent(0.33f)
        }

        setupWhetherRecycler()
        setupLocationRecycler()

        binding.sprinklerIcon.setOnClickListener {
            binding.sprinklerCheckBox.performClick()
        }

        binding.sprinklerCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            resetSprinklerCheckboxes()
            if (isChecked) {
                binding.sprinklerIcon.setImageDrawable(getDrawable(R.drawable.sprinkler_on))
            } else {
                binding.sprinklerIcon.setImageDrawable(getDrawable(R.drawable.sprinkler_off))
            }
        }

        if (savedInstanceState == null) {
            val currentForecastCallable = GetForecastDataCallable()
            val dailyForecastCallable = GetDailyForecastCallable()

            currentForecastCallable.setThreadPoolManager(ThreadPoolManager)
            dailyForecastCallable.setThreadPoolManager(ThreadPoolManager)

            ThreadPoolManager.setBindResultCallback(this)
            ThreadPoolManager.addCallable(currentForecastCallable)
            ThreadPoolManager.addCallable(dailyForecastCallable)
        } else {
            ThreadPoolManager.setBindResultCallback(this)
            ThreadPoolManager.activityReCreated()
        }


        Log.d("MainActivity: ", "OnCreate")
    }

    override fun onPause() {
        super.onPause()
        ThreadPoolManager.executorService.pause()
    }

    override fun onResume() {
        super.onResume()
        ThreadPoolManager.executorService.resume()
    }

    private fun getLocationsList(): List<Location> {
        return listOf(
            Location("Backyard", false, false),
            Location("Back Patio", false, false),
            Location("Front Yard", false, true),
            Location("Garden", false, false),
            Location("Porch", false, false)
        )
    }

    private fun setupLocationRecycler() {
        locationAdapter.bindLocationsList((getLocationsList()))
        binding.locationRv.apply {
            this.adapter = locationAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@UIPracticeActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupWhetherRecycler() {
        binding.weatherRv.adapter = whetherAdapter
    }

    private fun resetSprinklerCheckboxes() {
        locationAdapter.bindLocationsList(getLocationsList())
    }

    private fun setCurrentWeather(currentWeatherForecast: CurrentWeatherForecast) {
        binding.temperature.text =
            resources.getString(R.string.celcium_27, currentWeatherForecast.weather.temp)
        binding.humidity.text =
            resources.getString(R.string.percent_73, currentWeatherForecast.weather.humidity)
    }

    private fun setDailyForecastList(dailyForecastList: List<DailyForecast>) {
        whetherAdapter.bindWhetherList(dailyForecastList)
    }

    override fun bindCurrentWeatherToUi(currentWeather: CurrentWeatherForecast) {
        setCurrentWeather(currentWeather)
    }

    override fun bindDailyWeatherToUi(dailyWeather: List<DailyForecast>) {
        setDailyForecastList(dailyWeather)
    }
}