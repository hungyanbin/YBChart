package com.yanbin.chart

class BarChartViewModel {

    var barDatas: List<BarData> = listOf()
    var barDistance: Int = 0
    var barWidth: Int = 0
    var maxValue: Int = 0
    var labelHeight: Int = 0
    var highlightIndex = -1
    var xOffset = 0f
    var maxOffset = 0f
    var currentWidth = 0
    var currentHeight = 0

    var barRects: List<BarRect> = listOf()
    var barLabelVM: List<BarLabelVM> = listOf()

    fun onUpdateSize(width: Int, height: Int) {
        currentHeight = height
        currentWidth = width
        updateChart()
    }

    private fun updateChart() {
        maxOffset = ((barDistance + barWidth) * barDatas.size + barDistance).toFloat() - currentWidth
        barRects = barDatas.asSequence()
            .mapIndexed { index, barData ->
                generateRect(index, barData)
            }.toList()

        barLabelVM = barDatas.asSequence()
            .mapIndexed { index, barData ->
                generateBarLabelVM(index, barData.name)
            }.toList()
    }

    private fun generateBarLabelVM(index: Int, name: String): BarLabelVM {
        val centerX = barDistance * (index + 1) + barWidth * (index + 0.5f) - xOffset

        return BarLabelVM(name, centerX)
    }

    private fun generateRect(index: Int, barData: BarData): BarRect {
        val bottom = 0f
        val top = -(currentHeight * (barData.value / maxValue))
        val left = barDistance * (index + 1) + barWidth * index - xOffset
        val right = left + barWidth
        return BarRect(top, left, bottom, right)
    }

    fun onTapBarArea(x: Float, y: Float) {
        highlightIndex = barRects.indexOfFirst { rect ->
            rect.contains(x, y)
        }
    }

    fun onScroll(deltaX: Float) {
        when {
            maxOffset < currentWidth -> xOffset = 0f
            xOffset + deltaX < 0 -> xOffset = 0f
            xOffset + deltaX > maxOffset -> xOffset = maxOffset
            else -> xOffset += deltaX
        }
        updateChart()
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

class BarLabelVM(val text: String, val centerX: Float)