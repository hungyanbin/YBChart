package com.yanbin.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class BarChart : View {

    private val MAX_VALUE = 100

    private val data: List<BarData> = BarDataFactory.createRandomData(MAX_VALUE)
    private var boundaryPadding = 8.toPx().toFloat()
    private var barDistance = 16.toPx().toFloat()
    private var barWidth = 60.toPx()
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1.toPx().toFloat()
    }
    private val barPaint = Paint().apply {
        color = Color.RED
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        drawBoundary(canvas)
        drawBar(canvas)
    }

    private fun drawBar(canvas: Canvas) {
        data.forEachIndexed { index: Int, barData: BarData ->
            val left = boundaryPadding + index * barWidth + (index + 1) * barDistance
            val right = left + barWidth
            val bottom = height - boundaryPadding
            val top = bottom - bottom * (barData.value/ MAX_VALUE)
            val bar = RectF(left, top, right, bottom)
            canvas.drawRect(bar, barPaint)
        }
    }

    private fun drawBoundary(canvas: Canvas) {
        canvas.drawLine(boundaryPadding, height - boundaryPadding, width - boundaryPadding, height - boundaryPadding, linePaint)
        canvas.drawLine(boundaryPadding, height - boundaryPadding, boundaryPadding, boundaryPadding, linePaint)
    }
}