package com.stopkaaaa.swtec_practice.ui.custom_item

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
                ObjectAnimator.ofFloat(this, "translationX", -(2 * width / 3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
            false -> {
                ObjectAnimator.ofFloat(this, "translationX", -(width/3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
        }
    }

    private fun animateSwipeRight (fromDefault: Boolean) {
        when (fromDefault) {
            true -> {
                ObjectAnimator.ofFloat(this, "translationX", 0f).apply{
                    duration = 1000
                    start()
                }
            }
            false -> {
                ObjectAnimator.ofFloat(this, "translationX", -(width/3).toFloat()).apply{
                    duration = 1000
                    start()
                }
            }
        }

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

    fun setLocationTitle(title: String) {
        binding.location.text = title
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

    //    fun showLeftRect() {
//        val animator = ValueAnimator.ofInt(0, width)
//        animator.duration = 1000
//        animator.interpolator = DecelerateInterpolator()
//        animator.addUpdateListener { animation ->
//            val rectWidth = animation.animatedValue as Int
//            setLeftRectSize(rectWidth)
//        }
//        animator.start()
//    }
//
//    fun showRightRect() {
//        val animator = ValueAnimator.ofInt(width, 0)
//        animator.duration = 1000
//        animator.interpolator = DecelerateInterpolator()
//        animator.addUpdateListener { animation ->
//            val rectWidth = animation.animatedValue as Int
//            setRightRectSize(rectWidth)
//        }
//        animator.start()
//    }
}
