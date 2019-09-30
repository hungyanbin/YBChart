package com.yanbin.chart

import android.content.res.Resources
import kotlin.math.roundToInt

typealias Dp = Int

fun Dp.toPx(): Int {
    return (Resources.getSystem().displayMetrics.density * this).roundToInt()
}