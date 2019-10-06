package com.yanbin.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

abstract class PaddingFreeView : View {

    val canvasWidth
        get() = width - paddingLeft - paddingRight

    val canvasHeight
        get() = height - paddingTop - paddingBottom

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        canvas.clipRect(0, 0, width - paddingLeft - paddingRight, height - paddingTop - paddingBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val newWidth = (widthSize - paddingLeft - paddingRight).let {
            if (it < 0) width else it
        }

        val newHeight = (heightSize - paddingTop - paddingBottom).let {
            if (it < 0) width else it
        }

        onMeasureCanvas(
            MeasureSpec.makeMeasureSpec(newWidth, widthMode),
            MeasureSpec.makeMeasureSpec(newHeight, heightMode)
        )
    }

    abstract fun onMeasureCanvas(widthMeasureSpec: Int, heightMeasureSpec: Int)
}
