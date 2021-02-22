package com.stopkaaaa.swtec_practice.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.databinding.LocationCustomItemBinding
import com.stopkaaaa.swtec_practice.ui.custom_item.CustomItemState
import com.stopkaaaa.swtec_practice.ui.custom_item.OnSwipe
import com.stopkaaaa.swtec_practice.ui.custom_item.OnSwipeTouchListener

class LocationAdapter() : RecyclerView.Adapter<LocationViewHolder>() {

    private val locationsList: MutableList<Location> = mutableListOf()
    lateinit var locationsRecyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        locationsRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationCustomItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = LocationViewHolder(binding)

        holder.itemView.apply {
            layoutParams.height = ((locationsRecyclerView.measuredHeight -
                    parent.context.resources.displayMetrics.density * parent.context.resources.getDimension(
                R.dimen.margin_12
            )) / 5).toInt()
            layoutParams.width = locationsRecyclerView.measuredWidth * 3
            isFocusable = true
        }
        binding.item.x = - (holder.itemView.layoutParams.width / 3).toFloat()
        return holder
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.onBind(locationsList[position])
    }

    override fun getItemCount(): Int {
        return locationsList.size
    }

    fun bindLocationsList(newLocationsList: List<Location>) {
        locationsList.clear()
        locationsList.addAll(newLocationsList)
        notifyDataSetChanged()
    }
}

class LocationViewHolder(private val binding: LocationCustomItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun onBind(location: Location) {
        binding.item.setLocationTitle(location.title)
        binding.item.setCurrentSprinklingLocation(location.isSprinklingNow)
        binding.item.setFutureSprinklingLocation(location.isChosenToSprinkle)



//        val itemDescription = StringBuilder()
//        location.title.let{
//            binding.location.text = it
//            itemDescription.append(it).append(" ")
//        }
//
//        location.isSprinklingNow.let{
//            binding.currentSprinkleCheckBox.isChecked = it
//            if (it) {
//                itemDescription.append("sprinkling now").append(" ")
//            }
//            else {
//                itemDescription.append("doesn't sprinkling now").append(" ")
//            }
//        }
//
//        location.isChosenToSparkle.let{
//            binding.setSprinkleCheckBox.isChecked = it
//            if (it) {
//                itemDescription.append("and planned for tomorrow")
//            }
//            else {
//                itemDescription.append("and didn't planned for tomorrow")
//            }
//        }
//
//        updateItemContentDescription(itemDescription.toString())
//
//        binding.setSprinkleCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//            val itemDescription = StringBuilder()
//            itemDescription.append(binding.location.text)
//
//            if (binding.currentSprinkleCheckBox.isChecked) {
//                itemDescription.append("sprinkling now").append(" ")
//            }
//            else {
//                itemDescription.append("doesn't sprinkling now").append(" ")
//            }
//
//            if (isChecked) {
//                itemDescription.append("and planned for tomorrow")
//            }
//            else {
//                itemDescription.append("and didn't planned for tomorrow")
//            }
//
//            updateItemContentDescription(itemDescription.toString())
//        }


        binding.item.setOnTouchListener(OnSwipeTouchListener(binding.root.context, object :
            OnSwipe {
            override fun onSwipeLeft() {
                Toast.makeText(binding.root.context, "Left", Toast.LENGTH_SHORT).show()
                when (binding.item.currentItemState) {
                    CustomItemState.DEFAULT -> {
                        binding.item.setState(CustomItemState.DELETE)
                    }
                    CustomItemState.EDIT -> {
                        binding.item.setState(CustomItemState.DEFAULT)
                    }
                }
            }

            override fun onSwipeRight() {
                Toast.makeText(binding.root.context, "Right", Toast.LENGTH_SHORT).show()
                when (binding.item.currentItemState) {
                    CustomItemState.DEFAULT -> {
                        binding.item.setState(CustomItemState.EDIT)
                    }
                    CustomItemState.DELETE -> {
                        binding.item.setState(CustomItemState.DEFAULT)
                    }
                }
            }
        }))
    }

//    private fun updateItemContentDescription(itemDescription: String) {
//        binding.location.contentDescription = itemDescription
//    }

}