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
    private val labelTextPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.CENTER
        textSize = 12.toPx().toFloat()
    }
    private val valueTextPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.RIGHT
        textSize = 12.toPx().toFloat()
    }
    private var labelTextRect = Rect()
    private var valueTextRect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {

    }

    private fun getTextHeight(): Int {
        if (labelTextRect.height() == 0) {
            labelTextPaint.getTextBounds("Test", 0, 3, labelTextRect)
        }
        return labelTextRect.height()
    }

    override fun onDraw(canvas: Canvas) {
        drawBoundary(canvas)
        drawBar(canvas)
        drawLabel(canvas)
        drawValueText(canvas)
    }

    private fun drawValueText(canvas: Canvas) {
        val drawX = getValueWidth() - labelPadding + boundaryPadding
        val drawY = valueTextRect.height() / 2 + boundaryPadding
        canvas.drawText(MAX_VALUE.toString(), drawX, drawY, valueTextPaint)
    }

    private fun drawLabel(canvas: Canvas) {
        val textCenterY = height - labelPadding - getTextHeight() / 2

        data.forEachIndexed { index: Int, barData: BarData ->
            val textCenterX = boundaryPadding + (index + 0.5) * barWidth + (index + 1) * barDistance + getValueWidth()
            canvas.drawText(barData.name, textCenterX.toFloat(), textCenterY, labelTextPaint)
        }
    }

    private fun getValueWidth(): Float {
        if (valueTextRect.width() == 0) {
            val maxValueString = MAX_VALUE.toString()
            valueTextPaint.getTextBounds(maxValueString, 0, maxValueString.length, valueTextRect)
        }
        return valueTextRect.width() + labelPadding
    }

    private fun drawBar(canvas: Canvas) {
        data.forEachIndexed { index: Int, barData: BarData ->
            val left = boundaryPadding + index * barWidth + (index + 1) * barDistance + getValueWidth()
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
        canvas.drawLine(boundaryPadding + getValueWidth(),
            height - boundaryPadding - labelHeight,
            width - boundaryPadding + getValueWidth(),
            height - boundaryPadding - labelHeight,
            linePaint)
        //draw Y
        canvas.drawLine(boundaryPadding + getValueWidth(),
            height - boundaryPadding - labelHeight,
            boundaryPadding + getValueWidth(),
            boundaryPadding - labelHeight,
            linePaint)
    }

    private fun getLabelHeight() = getTextHeight() + labelPadding
}