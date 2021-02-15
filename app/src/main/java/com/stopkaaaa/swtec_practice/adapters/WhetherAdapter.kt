package com.stopkaaaa.swtec_practice.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.data.Whether
import com.stopkaaaa.swtec_practice.data.WhetherState
import com.stopkaaaa.swtec_practice.databinding.WhetherItemBinding

class WhetherAdapter() : RecyclerView.Adapter<WhetherViewHolder>() {

    private val whetherList: MutableList<Whether> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhetherViewHolder {
        val binding = WhetherItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = WhetherViewHolder(binding)
        holder.itemView.isFocusable = true
        return holder
    }

    override fun onBindViewHolder(holder: WhetherViewHolder, position: Int) {
        holder.onBind(whetherList[position])
    }

    override fun getItemCount(): Int {
        return whetherList.size
    }

    fun bindWhetherList(newWhetherList: List<Whether>) {
        whetherList.clear()
        whetherList.addAll(newWhetherList)
        notifyDataSetChanged()
    }
}

class WhetherViewHolder(private val binding: WhetherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun onBind(whether: Whether) {
        binding.date.text = whether.date
        binding.temperature.text = whether.temperature.toString() + "\u00B0"
        when (whether.icon) {
            WhetherState.RAIN -> binding.whetherIcon.apply {
                setImageDrawable(
                    binding.root.resources.getDrawable(
                        R.drawable.rain, binding.root.context.theme
                    )
                )
                contentDescription = "Possible rain"
            }

            WhetherState.CLOUDY -> binding.whetherIcon.apply {
                setImageDrawable(
                    binding.root.resources.getDrawable(
                        R.drawable.cloudy, binding.root.context.theme
                    )
                )
                contentDescription = "Mostly cloudy"
            }

            WhetherState.PARTLY_CLOUDY -> binding.whetherIcon.apply {
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