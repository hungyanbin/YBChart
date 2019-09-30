package com.yanbin.chart

import android.content.res.Resources
import android.graphics.Paint
import kotlin.math.roundToInt

typealias Dp = Int

fun Dp.toPx(): Int {
    return (Resources.getSystem().displayMetrics.density * this).roundToInt()
}

fun Paint.textHeight(): Int {
    val fm = this.fontMetrics
    return (fm.bottom - fm.top).toInt()
}
