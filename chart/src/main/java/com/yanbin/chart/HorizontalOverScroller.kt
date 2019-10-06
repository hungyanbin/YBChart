package com.yanbin.chart

import android.content.Context
import android.view.View
import android.widget.OverScroller
import androidx.core.view.ViewCompat

class HorizontalOverScroller(context: Context,
                             private val overRange: Int,
                             private val scrollView: View) {

    private val scroller: OverScroller = OverScroller(context)
    var onUpdatePosition: (Int) -> Unit = {}

    fun onFling(startPos: Int, maxPos: Int, velocity: Float) {
        val minX = 0
        scroller.forceFinished(true)
        scroller.fling(startPos, 0, -velocity.toInt(), 0, minX, maxPos, 0, 0, overRange, 0)
    }

    fun onScroll(startPos: Int, maxPos: Int, distance: Float) {
        if (startPos + distance < -overRange || startPos + distance > maxPos + overRange) {
            return
        }

        scroller.startScroll(startPos, 0, distance.toInt(), 0, 0)
    }

    fun onDown() {
        scroller.forceFinished(true)
    }

    fun onUp(maxPos: Int): Boolean {
        return if (scroller.currX < 0 || scroller.currX > maxPos) {
            val startX = scroller.currX
            val minX = 0
            val maxX = maxPos
            scroller.springBack(startX, 0, minX, maxX, 0, 0)
            ViewCompat.postInvalidateOnAnimation(scrollView)
            true
        } else {
            false
        }
    }

    fun computeScroll() {
        // The scroller isn't finished, meaning a fling or programmatic pan
        // operation is currently active.
        if (scroller.computeScrollOffset()) {
            val currX: Int = scroller.currX
            onUpdatePosition(currX)
            ViewCompat.postInvalidateOnAnimation(scrollView)
        }
    }
}