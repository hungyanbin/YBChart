package com.yanbin.chart

class BarChartViewModel {

    var barDatas: List<BarData> = listOf()
    var barDistance: Int = 0
    var barWidth: Int = 0
    var maxValue: Int = 0
    var labelHeight: Int = 0

    var barRects: List<BarRect> = listOf()
    var barLabelVM: List<BarLabelVM> = listOf()

    fun onMeasure(width: Int, height: Int) {
        barRects = barDatas.asSequence()
            .mapIndexed { index, barData ->
                generateRect(index, barData, height)
            }.toList()

        barLabelVM = barDatas.asSequence()
            .mapIndexed { index, barData ->
                generateBarLabelVM(index, barData.name)
            }.toList()
    }

    private fun generateBarLabelVM(index: Int, name: String): BarLabelVM {
        val centerX = barDistance * (index + 1) + barWidth * (index + 0.5f)
        val centerY = - labelHeight / 2f

        return BarLabelVM(name, centerX, centerY)
    }

    private fun generateRect(index: Int, barData: BarData, height: Int): BarRect {
        val bottom = 0f
        val top = - (height * (barData.value / maxValue))
        val left = barDistance * (index + 1) + barWidth * index
        val right = left + barWidth
        return BarRect(top, left.toFloat(), bottom, right.toFloat())
    }

    fun findBarByPosition(x: Float, y: Float): Int {
        return barRects.indexOfFirst { rect ->
            rect.contains(x, y)
        }
    }
}

class BarRect(val top: Float,
              val left: Float,
              val bottom: Float,
              val right: Float) {

    fun contains(x: Float, y: Float): Boolean {
        return (left < right && top < bottom  // check for empty first

            && x >= left && x < right && y >= top && y < bottom)
    }

}

class BarLabelVM(val text: String, val centerX: Float, val centerY: Float)