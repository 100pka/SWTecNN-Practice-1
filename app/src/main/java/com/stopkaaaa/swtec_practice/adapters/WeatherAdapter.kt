package com.stopkaaaa.swtec_practice.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.data.Weather
import com.stopkaaaa.swtec_practice.data.WeatherState
import com.stopkaaaa.swtec_practice.databinding.WeatherItemBinding

class WhetherAdapter() : RecyclerView.Adapter<WhetherViewHolder>() {

    private val weatherList: MutableList<Weather> = mutableListOf()

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

    fun bindWhetherList(newWeatherList: List<Weather>) {
        weatherList.clear()
        weatherList.addAll(newWeatherList)
        notifyDataSetChanged()
    }
}

class WhetherViewHolder(private val binding: WeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun onBind(weather: Weather) {
        binding.date.text = weather.date
        binding.temperature.text = weather.temperature.toString() + "\u00B0"
        when (weather.icon) {
            WeatherState.RAIN -> binding.weatherIcon.apply {
                setImageDrawable(
                    binding.root.resources.getDrawable(
                        R.drawable.rain, binding.root.context.theme
                    )
                )
                contentDescription = "Possible rain"
            }

            WeatherState.CLOUDY -> binding.weatherIcon.apply {
                setImageDrawable(
                    binding.root.resources.getDrawable(
                        R.drawable.cloudy, binding.root.context.theme
                    )
                )
                contentDescription = "Mostly cloudy"
            }

            WeatherState.PARTLY_CLOUDY -> binding.weatherIcon.apply {
                setImageDrawable(
                    binding.root.resources.getDrawable(
                        R.drawable.partly_cloudy, binding.root.context.theme
                    )
                )
                contentDescription = "Partly cloudy"
            }
        }
    }
}