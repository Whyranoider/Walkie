package com.whyranoid.data.model.running

data class RunningFinishRequest(
    val walkieId: Long,
    val authId: String,
    val historyId: Int,
    val endTime: String,
    val totalTime: Int,
    val distance: Double,
    val calorie: Int,
    val step: Int
)
