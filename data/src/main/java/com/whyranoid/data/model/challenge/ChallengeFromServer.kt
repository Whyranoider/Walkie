package com.whyranoid.data.model.challenge

data class ChallengeFromServer(
    val accCalories: Double?,
    val accCount: Int?,
    val accDistance: Double?,
    val accTime: String? = null,
    val badge: BadgeResponse,
    val calorie: Int,
    val category: String?,
    val challengeEdate: String? = null,
    val challengeId: Int,
    val challengeSdate: String? = null,
    val content: String,
    val distance: Int?,
    val endTime: String?,
    val goalCount: Int?,
    val img: String,
    val name: String,
    val period: Int?,
    val progress: Int?,
    val startTime: String?,
    val status: String?,
    val timeLimit: Int?
)