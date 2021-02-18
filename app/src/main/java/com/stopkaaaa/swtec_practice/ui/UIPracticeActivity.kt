package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smart.sprinkler.app.api.RetrofitClient
import smart.sprinkler.app.api.model.CurrentWeatherForecast
import smart.sprinkler.app.api.model.DailyForecast
import smart.sprinkler.app.api.model.WeatherForecast
import java.lang.Exception
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

class UIPracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUIPracticeBinding
    private val locationAdapter = LocationAdapter()
    private val whetherAdapter = WhetherAdapter()
    private val handlerThread = HandlerThread("backGround thread")
    private val uiHandler = Handler(Looper.getMainLooper())

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

        handlerThread.start()
        val backgroundHandler = Handler(handlerThread.looper)

        backgroundHandler.post {
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

            currentWeather?.let {
                uiHandler.post {
                    setCurrentWeather(it)
                }
            }

            if (dailyWeather.isNotEmpty()) {
                uiHandler.post {
                    setDailyForecastList(dailyWeather.subList(0, 5))
                }
            }
        }
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
}