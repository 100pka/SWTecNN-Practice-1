package com.stopkaaaa.swtec_practice.ui.custom_item

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context, val onSwipeCallbacks: OnSwipe) : View.OnTouchListener {

    companion object {
        const val SWIPE_DISTANCE_THRESHOLD = 50
        const val SWIPE_VELOCITY_THRESHOLD = 50
    }

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = (e1?.x?.let { e2?.x?.minus(it) })
            val distanceY = (e1?.y?.let { e2?.y?.minus(it) })

            if ( distanceX != null && distanceY != null ) {
                if ( (abs(distanceX) > abs(distanceY))
                    && (abs(distanceX) > SWIPE_DISTANCE_THRESHOLD)
                    && (abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)) {
                    if (distanceX > 0) {
                        onSwipeCallbacks.onSwipeRight()
                    } else {
                        onSwipeCallbacks.onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }
}