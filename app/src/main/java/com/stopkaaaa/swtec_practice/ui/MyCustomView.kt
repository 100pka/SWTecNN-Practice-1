package com.stopkaaaa.swtec_practice.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.stopkaaaa.swtec_practice.R
import com.stopkaaaa.swtec_practice.databinding.MyCustomViewBinding

@SuppressLint("Recycle")
class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var binding: MyCustomViewBinding

    private var notifyCountTextColor = 0
    private var notifyCountTextSize = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView)
        notifyCountTextColor = typedArray.getColor(R.styleable.MyCustomView_count_text_color, Color.WHITE)
        notifyCountTextSize = typedArray.getDimension(R.styleable.MyCustomView_count_text_size, 12f)

        binding = MyCustomViewBinding.inflate(LayoutInflater.from(context), this)

        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        binding.notifyCount.width = width/3
        binding.notifyCount.height = width/3
        binding.notifyCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, notifyCountTextSize)
        binding.notifyCount.setTextColor(notifyCountTextColor)
        contentDescription = "You have ${binding.notifyCount.text} notifications"

    }

}