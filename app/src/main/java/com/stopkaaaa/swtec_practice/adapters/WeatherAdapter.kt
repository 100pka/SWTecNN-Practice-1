package com.stopkaaaa.swtec_practice.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.data.Weather
import com.stopkaaaa.swtec_practice.data.WeatherState
import com.stopkaaaa.swtec_practice.databinding.WeatherItemBinding
import smart.sprinkler.app.api.model.DailyForecast
import kotlin.math.roundToInt

const val MAX_ITEMS_TO_SHOW = 5

class WhetherAdapter() : RecyclerView.Adapter<WhetherViewHolder>() {


    private val weatherList: MutableList<DailyForecast> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhetherViewHolder {
        val binding = WeatherItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = WhetherViewHolder(binding)
        holder.itemView.isFocusable = true
        return holder
    }

    override fun onBindViewHolder(holder: WhetherViewHolder, position: Int) {
        holder.onBind(weatherList[position])
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun bindWhetherList(newWeatherList: List<DailyForecast>) {
        weatherList.clear()
        weatherList.addAll(newWeatherList)
        notifyDataSetChanged()
    }
}

class WhetherViewHolder(private val binding: WeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun onBind(weather: DailyForecast) {
        binding.date.text = weather.getDate()
        if (weather.temp.day > 0) {
            binding.temperature.text = "+" + (weather.temp.day).roundToInt().toString() + "\u00B0"
        } else {
            binding.temperature.text = (weather.temp.day).roundToInt().toString() + "\u00B0"
        }

        Glide.with(binding.root.context)
            .load(weather.weatherImage[0].getIconUrl())
            .into(binding.weatherIcon)
    }
}