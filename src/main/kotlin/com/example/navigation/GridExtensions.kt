package com.example.navigation

import kotlin.math.roundToInt


    fun Double.toGrid(): Int =
        (this * 2).roundToInt()

   fun Double.roundToHalf(): Double =
    kotlin.math.round(this * 2.0) / 2.0