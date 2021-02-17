package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.api.CurrentWeatherForecastLoader
import com.stopkaaaa.swtec_practice.api.WeatherForecastLoader
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import smart.sprinkler.app.api.model.WeatherForecast
import java.lang.RuntimeException

const val CURRENT_WEATHER_LOADER_ID = 52
const val DAILY_WEATHER_LOADER_ID = 152


class UIPracticeActivity : AppCompatActivity() {

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

        val currentWeatherForecastLoader = CurrentWeatherForecastLoaderCallbacks()
        val dailyWeatherForecastLoader = WeatherForecastLoaderCallbacks()

        supportLoaderManager.initLoader(CURRENT_WEATHER_LOADER_ID, Bundle(), currentWeatherForecastLoader)
        supportLoaderManager.initLoader(DAILY_WEATHER_LOADER_ID, Bundle(), dailyWeatherForecastLoader)

        Log.d("MainActivity: ", "OnCreate")
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

    private fun showMessageToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    inner class CurrentWeatherForecastLoaderCallbacks :
        LoaderManager.LoaderCallbacks<CurrentWeatherForecast> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<CurrentWeatherForecast> {
            if (id == CURRENT_WEATHER_LOADER_ID) {
                return CurrentWeatherForecastLoader(this@UIPracticeActivity)
            }
            throw RuntimeException("Wrong Loader ID")
        }

        override fun onLoadFinished(
            loader: Loader<CurrentWeatherForecast>,
            data: CurrentWeatherForecast?
        ) {
            data?.let { setCurrentWeather(it) }
        }

        override fun onLoaderReset(loader: Loader<CurrentWeatherForecast>) {

        }
    }

    inner class WeatherForecastLoaderCallbacks :
        LoaderManager.LoaderCallbacks<WeatherForecast> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<WeatherForecast> {
            if (id == DAILY_WEATHER_LOADER_ID) {
                return WeatherForecastLoader(this@UIPracticeActivity)
            }
            throw RuntimeException("Wrong Loader ID")
        }

        override fun onLoadFinished(
            loader: Loader<WeatherForecast>,
            data: WeatherForecast?
        ) {
            data?.let { setDailyForecastList(it.daily.subList(0, 5)) }
        }

        override fun onLoaderReset(loader: Loader<WeatherForecast>) {

        }
    }
}