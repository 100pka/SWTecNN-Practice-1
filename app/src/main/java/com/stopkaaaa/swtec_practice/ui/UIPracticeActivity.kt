package com.stopkaaaa.swtec_practice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.adapters.LocationAdapter
import com.stopkaaaa.swtec_practice.adapters.WhetherAdapter
import com.stopkaaaa.swtec_practice.data.Location
import com.stopkaaaa.swtec_practice.data.Whether
import com.stopkaaaa.swtec_practice.data.WhetherState
import com.stopkaaaa.swtec_practice.databinding.ActivityUIPracticeBinding

class UIPracticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityUIPracticeBinding
    private val locationAdapter = LocationAdapter()
    private val whetherAdapter = WhetherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUIPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        Log.d("MainActivity: ", "OnCreate" )
    }

    private fun getWhetherList() : List<Whether> {
        return listOf(
            Whether("February 7, 2020", 23, WhetherState.RAIN),
            Whether("February 8, 2020", 23, WhetherState.CLOUDY),
            Whether("February 9, 2020", 25, WhetherState.PARTLY_CLOUDY)
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
        whetherAdapter.bindWhetherList(getWhetherList())
        binding.whetherRv.adapter = whetherAdapter
    }

    private fun resetSprinklerCheckboxes() {
        locationAdapter.bindLocationsList(getLocationsList())
    }
}