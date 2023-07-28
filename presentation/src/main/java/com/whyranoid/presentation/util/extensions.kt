package com.whyranoid.presentation.util

import android.content.Context
import kotlin.math.min
import kotlin.random.Random

fun <T> List<T>.chunkedList(size: Int): List<List<T>> {
    val newList = mutableListOf<List<T>>()
    var index = 0
    while (index < this.size) {
        val chunk = this.subList(index, min(index + size, this.size))
        newList.add(chunk)
        index += size
    }
    return newList
}

fun ClosedFloatingPointRange<Float>.random(): Float {
    return Random.nextDouble(start.toDouble(), endInclusive.toDouble()).toFloat()
}

fun Int.toRunningTime(): String {
    return "${"%02d".format(this.div(3600))}:${
        "%02d".format(
            this.rem(3600).div(60),
        )
    }:${"%02d".format(this.rem(60))}"
}

fun Double.toPace(): String {
    return "%.1f".format(this).replace('.', '`') + "``"
}

fun Int.dpToPx(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.pxToDp(context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    val mul = when (scale) {
        1.0f -> 4.0f
        1.5f -> 8 / 3.0f
        2.0f -> 2.0f
        else -> 1.0f
    }
    return (this / (scale * mul)).toInt()
}
