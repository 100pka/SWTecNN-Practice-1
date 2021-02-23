package com.stopkaaaa.swtec_practice.ui.custom_item

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getSystemService
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.databinding.CustomItemBinding


class CustomItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: CustomItemBinding

    private var locationTitle = ""

    var currentItemState = CustomItemState.DEFAULT

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomItem)

        locationTitle = typedArray.getString(R.styleable.CustomItem_location_title) ?: ""

        binding = CustomItemBinding.inflate(LayoutInflater.from(context), this)

        setWillNotDraw(false)

        typedArray.recycle()

        setLocationTitle(locationTitle)
    }

    private fun animateSwipeLeft (fromDefault: Boolean) {
        when (fromDefault) {
            true -> {
                ObjectAnimator.ofFloat(binding.deleteState, "translationX", -(width / 3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
            false -> {
                ObjectAnimator.ofFloat(binding.editState, "translationX", -(width / 3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
        }
//        when (fromDefault) {
//            true -> {
//                ObjectAnimator.ofFloat(this, "translationX", -(2 * width / 3).toFloat()).apply{
//                    duration = 1000
//                    start()
//                }
//            }
//            false -> {
//                ObjectAnimator.ofFloat(this, "translationX", -(width/3).toFloat()).apply{
//                    duration = 1000
//                    start()
//                }
//            }
//        }
    }

    private fun animateSwipeRight (fromDefault: Boolean) {
        when (fromDefault) {
            true -> {
                ObjectAnimator.ofFloat(binding.editState, "translationX", (width/3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
            false -> {
                ObjectAnimator.ofFloat(binding.deleteState, "translationX", (width/3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
        }
//
//        when (fromDefault) {
//            true -> {
//                ObjectAnimator.ofFloat(this, "translationX", 0f).apply{
//                    duration = 1000
//                    start()
//                }
//            }
//            false -> {
//                ObjectAnimator.ofFloat(this, "translationX", -(width/3).toFloat()).apply{
//                    duration = 1000
//                    start()
//                }
//            }
//        }
    }

    fun setState(itemState: CustomItemState) {
        when (currentItemState) {
            CustomItemState.DEFAULT -> {
                when (itemState) {
                    CustomItemState.EDIT -> {
                        animateSwipeRight(true)
                    }
                    CustomItemState.DELETE -> {
                        animateSwipeLeft(true)
                    }
                    else -> {}
                }
            }
            CustomItemState.EDIT -> {
                when (itemState) {
                    CustomItemState.DEFAULT -> {
                        binding.location.text = binding.editText.text.toString()

                        val inputMethodManager = binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

                        animateSwipeLeft(false)
                    }
                    else -> {}
                }
            }
            CustomItemState.DELETE -> {
                when (itemState) {
                    CustomItemState.DEFAULT -> {
                        animateSwipeRight(false)
                    }
                    else -> {}
                }
            }
        }
        currentItemState = itemState
    }

    fun setDeleteClickListener(listener: ((View) -> Unit)?) {
        binding.deleteBtn.setOnClickListener (listener)
    }

    fun setLocationTitle(title: String) {
        binding.location.text = title
        binding.editText.text.replace(0, binding.editText.text.length, title)
    }

    fun setCurrentSprinklingLocation (isSprinklingNow: Boolean) {
        binding.currentSprinkleCheckBox.isChecked = isSprinklingNow
    }

    fun setFutureSprinklingLocation ( isPlanedToSprinkle: Boolean) {
        binding.setSprinkleCheckBox.isChecked = isPlanedToSprinkle
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
