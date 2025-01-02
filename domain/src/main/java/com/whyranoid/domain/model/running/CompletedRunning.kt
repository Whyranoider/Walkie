package com.whyranoid.domain.model.running

data class CompletedRunning(
    val calorie: Int?,
    val category: String?,
    val challengeId: Int?,
    val distance: Int?,
    val endTime: String?,
    val goalCount: Int?,
    val limitPerDay: Int?,
    val name: String?,
    val newFlag: Int?,
    val period: Int?,
    val progress: Int?,
    val startTime: String?,
    val status: String?,
    val time: Int?,
    val timeLimit: Int?
)
