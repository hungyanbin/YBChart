package com.yanbin.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BarChart : View {

    private val MAX_VALUE = 100

    private val data: List<BarData> = BarDataFactory.createRandomData(MAX_VALUE)
    private var boundaryPadding = 8.toPx().toFloat()
    private var labelPadding = 8.toPx().toFloat()
    private var barDistance = 16.toPx().toFloat()
    private var barWidth = 60.toPx()
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1.toPx().toFloat()
    }
    private val barPaint = Paint().apply {
        color = Color.RED
    }
    private val textPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.CENTER
        textSize = 12.toPx().toFloat()
    }
    private var textRect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {

    }

    private fun getTextHeight(): Int {
        if (textRect.height() == 0) {
            textPaint.getTextBounds("Test", 0, 3, textRect)
        }
        return textRect.height()
    }

    override fun onDraw(canvas: Canvas) {
        drawBoundary(canvas)
        drawBar(canvas)
        drawLabel(canvas)
    }

    private fun drawLabel(canvas: Canvas) {
        val textCenterY = height - labelPadding - getTextHeight() / 2

        data.forEachIndexed { index: Int, barData: BarData ->
            val textCenterX = boundaryPadding + (index + 0.5) * barWidth + (index + 1) * barDistance
            canvas.drawText(barData.name, textCenterX.toFloat(), textCenterY, textPaint)
        }
    }

    private fun drawBar(canvas: Canvas) {
        data.forEachIndexed { index: Int, barData: BarData ->
            val left = boundaryPadding + index * barWidth + (index + 1) * barDistance
            val right = left + barWidth
            val bottom = height - boundaryPadding - getLabelHeight()
            val top = bottom - bottom * (barData.value / MAX_VALUE)
            val bar = RectF(left, top, right, bottom)
            canvas.drawRect(bar, barPaint)
        }
    }

    private fun drawBoundary(canvas: Canvas) {
        val labelHeight = getLabelHeight()
        //draw X
        canvas.drawLine(boundaryPadding,
            height - boundaryPadding - labelHeight,
            width - boundaryPadding,
            height - boundaryPadding - labelHeight,
            linePaint)
        //draw Y
        canvas.drawLine(boundaryPadding,
            height - boundaryPadding - labelHeight,
            boundaryPadding,
            boundaryPadding - labelHeight,
            linePaint)
    }

    private fun getLabelHeight() = getTextHeight() + labelPadding
}