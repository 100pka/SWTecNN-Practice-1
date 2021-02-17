package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import smart.sprinkler.app.api.model.WeatherForecast
import java.lang.Exception
import java.lang.ref.WeakReference

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
        CurrentWeatherForecastAsync(this).execute()
        DailyWeatherForecastAsync(this).execute()

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

    companion object {
        class CurrentWeatherForecastAsync(context: UIPracticeActivity) : AsyncTask<Void, Void, CurrentWeatherForecast?>() {

            private val activityReference: WeakReference<UIPracticeActivity> = WeakReference(context)
            var currentWeather : CurrentWeatherForecast? = null

            override fun doInBackground(vararg params: Void?): CurrentWeatherForecast? {
                try {
                    currentWeather = RetrofitClient.getCurrentWeather().execute().body()
                } catch (e: Exception) {
                    Log.d("Retrofit onFailure: ", e.message.toString())
                }
                return currentWeather
            }

            override fun onPostExecute(result: CurrentWeatherForecast?) {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                result?.let { activity.setCurrentWeather(it) }
            }
        }

        class DailyWeatherForecastAsync(context: UIPracticeActivity) : AsyncTask<Void, Void, WeatherForecast?>() {

            private val activityReference: WeakReference<UIPracticeActivity> = WeakReference(context)
            var dailyWeather : WeatherForecast? = null

            override fun doInBackground(vararg params: Void?): WeatherForecast? {
                try {
                    dailyWeather = RetrofitClient.getWeatherForecast().execute().body()
                } catch (e: Exception) {
                    Log.d("Retrofit onFailure: ", e.message.toString())
                }
                return dailyWeather
            }

            override fun onPostExecute(result: WeatherForecast?) {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                result?.let { activity.setDailyForecastList(it.daily.subList(0, 5)) }
            }
        }
    }
}