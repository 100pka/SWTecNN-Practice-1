package com.stopkaaaa.swtec_practice.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.databinding.MyCustomViewBinding

class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: MyCustomViewBinding

    private var notifyCountTextColor = 0
    private var notifyCountTextSize = 0f
    private val paintLeft = Paint()
    private val paintRight = Paint()
    private var leftRectWidth = 0
    private var rightRectWidth = Int.MAX_VALUE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView)
        notifyCountTextColor = typedArray.getColor(
            R.styleable.MyCustomView_count_text_color,
            Color.WHITE
        )
        notifyCountTextSize = typedArray.getDimension(R.styleable.MyCustomView_count_text_size, 12f)

        binding = MyCustomViewBinding.inflate(LayoutInflater.from(context), this)

        setWillNotDraw(false)

        typedArray.recycle()
        paintLeft.color = Color.GREEN
        paintRight.color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        binding.notifyCount.width = width/3
        binding.notifyCount.height = width/3
        binding.notifyCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, notifyCountTextSize)
        binding.notifyCount.setTextColor(notifyCountTextColor)
        contentDescription = "You have ${binding.notifyCount.text} notifications"
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        val leftRect = Rect(0, 0, leftRectWidth, height)
        canvas?.drawRect(leftRect, paintLeft)
        val rightRect = Rect(rightRectWidth, 0, width, height)
        canvas?.drawRect(rightRect, paintRight)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun setLeftRectSize(rectWidth: Int) {
        this.leftRectWidth = rectWidth
        this.invalidate()
    }

    fun setRightRectSize(rectWidth: Int) {
        this.rightRectWidth = rectWidth
        this.invalidate()
    }

    fun showLeftRect() {
        val animator = ValueAnimator.ofInt(0, width)
        animator.duration = 1000
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val rectWidth = animation.animatedValue as Int
            setLeftRectSize(rectWidth)
        }
        animator.start()
    }

    fun showRightRect() {
        val animator = ValueAnimator.ofInt(width, 0)
        animator.duration = 1000
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val rectWidth = animation.animatedValue as Int
            setRightRectSize(rectWidth)
        }
        animator.start()
    }
}