package com.yanbin.chart

import kotlin.random.Random

class BarDataFactory {

    companion object {

        fun createRandomData(range: Int): List<BarData> {
            val random = Random(System.currentTimeMillis())
            val names = listOf("2000", "2001", "2002", "2003", "2004")
            return (0..4).map {
                val value = random.nextFloat() * range
                val name = names[it]
                BarData(name, value)
            }
        }
    }
}