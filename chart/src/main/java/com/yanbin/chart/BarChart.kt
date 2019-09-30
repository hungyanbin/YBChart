package com.yanbin.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class BarChart : View {

    private val MAX_VALUE = 100

    private val data: List<BarData> = BarDataFactory.createRandomData(MAX_VALUE)
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
        textSize = 30.toPx().toFloat()
    }
    private val valueTextPaint = Paint().apply {
        color = Color.GRAY
        textAlign = Paint.Align.RIGHT
        textSize = 30.toPx().toFloat()
    }
    private var valueTextRect = Rect()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {

    }

    private fun getLabelTextHeight(): Int {
        return labelTextPaint.textHeight()
    }

    override fun onDraw(canvas: Canvas) {
        drawBoundary(canvas)
        drawBar(canvas)
        drawLabel(canvas)
        drawValueText(canvas)
    }

    private fun drawValueText(canvas: Canvas) {
        val drawX = getValueWidth() - labelPadding + paddingLeft
        val drawY = valueTextPaint.textHeight() / 2 + paddingTop
        canvas.drawText(MAX_VALUE.toString(), drawX, drawY.toFloat(), valueTextPaint)
    }

    private fun drawLabel(canvas: Canvas) {
        canvas.save()
        canvas.translate(paddingLeft + getValueWidth() + barDistance + 0.5f * barWidth
            , (height - paddingBottom - getLabelTextHeight() / 2).toFloat())

        data.forEachIndexed { index: Int, barData: BarData ->
            val textCenterX = index * (barWidth + barDistance)
            canvas.drawText(barData.name, textCenterX, 0f, labelTextPaint)
        }
        canvas.restore()
    }

    private fun getValueWidth(): Float {
        if (valueTextRect.width() == 0) {
            val maxValueString = MAX_VALUE.toString()
            valueTextPaint.getTextBounds(maxValueString, 0, maxValueString.length, valueTextRect)
        }
        return valueTextRect.width() + labelPadding
    }

    private fun drawBar(canvas: Canvas) {
        canvas.save()
        canvas.translate(paddingLeft + barDistance + getValueWidth(),
            height - paddingBottom - getLabelHeight())
        data.forEachIndexed { index: Int, barData: BarData ->
            val left = index * (barWidth + barDistance)
            val right = left + barWidth
            val bottom = 0f
            val top = - (height - getLabelHeight() - paddingBottom - paddingTop) * (barData.value / MAX_VALUE)
            val bar = RectF(left, top, right, bottom)
            canvas.drawRect(bar, barPaint)
        }
        canvas.restore()
    }

    private fun drawBoundary(canvas: Canvas) {
        val labelHeight = getLabelHeight()
        //draw X
        canvas.save()
        canvas.translate(getValueWidth() + paddingLeft, height - paddingBottom - labelHeight)
        val xLength = width - getValueWidth() - paddingLeft - paddingRight
        canvas.drawLine(0f, 0f, xLength, 0f, linePaint)
        canvas.restore()
        //draw Y
        canvas.save()
        canvas.translate(paddingLeft + getValueWidth(), paddingTop.toFloat())
        val yLength = height - labelHeight - paddingTop - paddingBottom
        canvas.drawLine(0f, 0f, 0f, yLength, linePaint)
        canvas.restore()
    }

    private fun getLabelHeight() = getLabelTextHeight() + labelPadding
}