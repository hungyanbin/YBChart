package com.yanbin.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.yanbin.widget.PaddingFreeView

class BarChart : PaddingFreeView {

    private val MAX_VALUE = 100
    private val defaultTextSize = 30.toPx().toFloat()
    private val defaultColor = Color.GRAY

    private val data: List<BarData> = BarDataFactory.createRandomData(MAX_VALUE)

    private var labelPadding = 8.toPx().toFloat()
    private var barColor = Color.RED
    private var barHighlightColor = Color.MAGENTA
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1.toPx().toFloat()
    }
    private val barPaint = Paint().apply {
        color = barColor
    }
    private val labelTextPaint = Paint().apply {
        color = defaultColor
        textAlign = Paint.Align.CENTER
        textSize = defaultTextSize
    }
    private val valueTextPaint = Paint().apply {
        color = defaultColor
        textAlign = Paint.Align.RIGHT
        textSize = defaultTextSize
    }
    private var valueTextRect = Rect()
    private val barChartViewModel = BarChartViewModel()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init(context, attributeSet)
    }

    override fun onMeasureCanvas(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val newWidth = measureWithSpec(widthMeasureSpec) {
            (getValueWidth() +
                (barChartViewModel.barWidth + barChartViewModel.barDistance) * 3).toInt()
        }
        //FIXME Magic number for minHeight
        val newHeight = measureWithSpec(heightMeasureSpec) {
            (getLabelHeight() + 200.toPx()).toInt()
        }
        setMeasuredDimension(newWidth, newHeight)

        barChartViewModel.onMeasure((canvasWidth - getValueWidth()).toInt(),
            (canvasHeight - getLabelHeight()).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBoundary(canvas)
        drawBar(canvas)
        drawLabel(canvas)
        drawValueText(canvas)
    }

    private fun init(context: Context, attributeSet: AttributeSet?) {
        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onShowPress(e: MotionEvent?) {
            }


            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                val offsetX = getValueWidth()
                val offsetY = canvasHeight - getLabelHeight()
                val x = e.x - offsetX
                val y = e.y - offsetY
                barChartViewModel.onTapBarArea(x, y)
                postInvalidate()
                return true
            }
        }
        val gestureDetector = GestureDetector(context, gestureListener)
        setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        isClickable = true
        isFocusable = true

        if (attributeSet == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BarChart)
        linePaint.color = typedArray.getColor(R.styleable.BarChart_boundaryColor, defaultColor)
        labelTextPaint.color = typedArray.getColor(R.styleable.BarChart_labelTextColor, defaultColor)
        labelTextPaint.textSize = typedArray.getDimension(R.styleable.BarChart_labelTextSize, defaultTextSize)
        valueTextPaint.color = typedArray.getColor(R.styleable.BarChart_valueTextColor, defaultColor)
        valueTextPaint.textSize = typedArray.getDimension(R.styleable.BarChart_valueTextSize, defaultTextSize)
        typedArray.recycle()

        with(barChartViewModel) {
            barDatas = data
            barWidth = 60.toPx()
            barDistance = 16.toPx()
            maxValue = MAX_VALUE
            labelHeight = getLabelTextHeight()
        }
    }

    private fun getLabelTextHeight(): Int {
        return labelTextPaint.textHeight()
    }

    private fun measureWithSpec(measureSpec: Int, minValueFun: () -> Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        val minValue = minValueFun.invoke()

        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> if (specSize < minValue) specSize else minValue
            MeasureSpec.UNSPECIFIED -> minValue
            else -> 0
        }
    }

    private fun drawValueText(canvas: Canvas) {
        val drawX = getValueWidth() - labelPadding
        val drawY = valueTextPaint.textTopDistance()
        canvas.drawText(MAX_VALUE.toString(), drawX, drawY.toFloat(), valueTextPaint)
    }

    private fun drawLabel(canvas: Canvas) {
        canvas.save()
        canvas.translate(getValueWidth()
            , canvasHeight.toFloat())

        canvas.clipRect(0, 0,
            (canvasWidth - getValueWidth()).toInt(),
            -getLabelHeight().toInt())

        barChartViewModel
            .barLabelVM
            .forEach { labelVM: BarLabelVM ->
                canvas.drawText(labelVM.text, labelVM.centerX, (- labelTextPaint.textBottomDistance()).toFloat(), labelTextPaint)
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
        canvas.translate(getValueWidth(),
            canvasHeight - getLabelHeight())
        canvas.clipRect(0, 0,
            (canvasWidth - getValueWidth()).toInt(),
            -(canvasHeight - getLabelHeight().toInt()))

        barChartViewModel.barRects
            .forEachIndexed { index, barRect ->
                if (index == barChartViewModel.highlightIndex) {
                    barPaint.color = barHighlightColor
                } else {
                    barPaint.color = barColor
                }
                canvas.drawRect(barRect.left, barRect.top, barRect.right, barRect.bottom, barPaint)
            }
        canvas.restore()
    }

    private fun drawBoundary(canvas: Canvas) {
        val labelHeight = getLabelHeight()
        //draw X
        canvas.save()
        canvas.translate(getValueWidth(), canvasHeight - labelHeight)
        val xLength = canvasWidth - getValueWidth()
        canvas.drawLine(0f, 0f, xLength, 0f, linePaint)
        canvas.restore()
        //draw Y
        canvas.save()
        canvas.translate(getValueWidth(), 0f)
        val yLength = canvasHeight - labelHeight
        canvas.drawLine(0f, 0f, 0f, yLength, linePaint)
        canvas.restore()
    }

    private fun getLabelHeight() = getLabelTextHeight() + labelPadding

}