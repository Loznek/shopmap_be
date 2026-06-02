package com.example.navigation

import kotlin.math.roundToInt


    fun Double.toGrid(): Int =
        (this * 2).roundToInt()
