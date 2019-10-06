package com.yanbin.chart

import kotlin.random.Random

class BarDataFactory {

    companion object {

        fun createRandomData(range: Int): List<BarData> {
            val random = Random(System.currentTimeMillis())
            return (0..20).map {
                val value = random.nextFloat() * range
                val name = 2000 + it
                BarData(name.toString(), value)
            }
        }
    }
}