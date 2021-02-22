package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.data.Weather
import com.stopkaaaa.swtec_practice.data.WeatherState
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding
import com.stopkaaaa.swtec_practice.ui.custom_item.OnSwipe
import com.stopkaaaa.swtec_practice.ui.custom_item.OnSwipeTouchListener
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast

class UIPracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUIPracticeBinding
    private val viewModel: UIPracticeActivityViewModel by viewModels()
    private val locationAdapter = LocationAdapter()
    private val whetherAdapter = WhetherAdapter()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUIPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> binding.horizontalGuideline.setGuidelinePercent(0.50f)
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

        binding.myCustomView.setOnTouchListener(
            OnSwipeTouchListener(
                this@UIPracticeActivity,
                object : OnSwipe {
                    override fun onSwipeLeft() {
                        Toast.makeText(this@UIPracticeActivity, "Left", Toast.LENGTH_SHORT).show()
                        binding.myCustomView.showRightRect()
                    }

                    override fun onSwipeRight() {
                        Toast.makeText(this@UIPracticeActivity, "Right", Toast.LENGTH_SHORT).show()
                        binding.myCustomView.showLeftRect()
                    }

                })
        )

        viewModel.currentWeather.observe(this, this::setCurrentWeather)
        viewModel.dailyForecastList.observe(this, this::setDailyForecastList)
        viewModel.currentWeatherLoadingState.observe(this, this::setLoadingCurrentWeather)
        viewModel.dailyWeatherLoadingState.observe(this, this::setLoadingDailyWeather)

        Log.d("MainActivity: ", "OnCreate" )
    }

    private fun getWhetherList() : List<Weather> {
        return listOf(
            Weather("February 7, 2020", 23, WeatherState.RAIN),
            Weather("February 8, 2020", 23, WeatherState.CLOUDY),
            Weather("February 9, 2020", 25, WeatherState.PARTLY_CLOUDY)
        )
    }

    private fun getLocationsList() : List<Location> {
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
            addItemDecoration(DividerItemDecoration(this@UIPracticeActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupWhetherRecycler() {
        binding.weatherRv.adapter = whetherAdapter
    }

    private fun resetSprinklerCheckboxes() {
        locationAdapter.bindLocationsList(getLocationsList())
    }

    private fun setCurrentWeather(currentWeatherForecast: CurrentWeatherForecast) {
        binding.temperature.text = resources.getString(R.string.celcium_27, currentWeatherForecast.weather.temp)
        binding.humidity.text = resources.getString(R.string.percent_73, currentWeatherForecast.weather.humidity)
    }

    private fun setDailyForecastList(dailyForecastList: List<DailyForecast>) {
        whetherAdapter.bindWhetherList(dailyForecastList)
    }

    private fun setLoadingCurrentWeather(currentWeatherLoadingState: CurrentWeatherLoadingState) {
        when (currentWeatherLoadingState) {
            CurrentWeatherLoadingState.LOADING -> {
                binding.temperature.isInvisible = true
                binding.humidity.isInvisible = true
                binding.temperatureProgress.isVisible = true
                binding.humidityProgress.isVisible = true
            }
            CurrentWeatherLoadingState.DONE -> {
                binding.temperature.isInvisible = false
                binding.humidity.isInvisible = false
                binding.temperatureProgress.isVisible = false
                binding.humidityProgress.isVisible = false
            }

            CurrentWeatherLoadingState.ERROR -> {
                binding.temperature.isInvisible = true
                binding.humidity.isInvisible = true
                binding.temperatureProgress.isVisible = false
                binding.humidityProgress.isVisible = false
                Toast.makeText(this, "Something went wrong while loading", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoadingDailyWeather(dailyWeatherLoadingState: DailyWeatherLoadingState) {
        when (dailyWeatherLoadingState) {
            DailyWeatherLoadingState.LOADING -> {
                binding.weatherRv.isInvisible = true
                binding.weatherRvProgress.isVisible = true
            }
            DailyWeatherLoadingState.DONE -> {
                binding.weatherRv.isInvisible = false
                binding.weatherRvProgress.isVisible = false
            }

            DailyWeatherLoadingState.ERROR -> {
                binding.weatherRv.isInvisible = true
                binding.weatherRvProgress.isVisible = false
                Toast.makeText(this, "Something went wrong while loading", Toast.LENGTH_LONG).show()
            }
        }
    }
}