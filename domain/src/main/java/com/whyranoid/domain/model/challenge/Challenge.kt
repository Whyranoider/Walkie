package com.whyranoid.domain.model.challenge

import com.whyranoid.domain.model.user.User

data class Challenge(
    val accCalories: Double?,
    val accCount: Int?,
    val accDistance: Double?,
    val accTime: String?,
    val badge: Badge,
    val calorie: Int,
    val challengeType: ChallengeType,
    val challengeEdate: String?,
    val id: Int,
    val challengeSdate: String?,
    val content: String,
    val distance: Int?,
    val endTime: String?,
    val goalCount: Int?,
    val imageUrl: String,
    val name: String,
    val period: Int?,
    val progress: Int?,
    val startTime: String?,
    val status: String?,
    val timeLimit: Int?,
    val time: Int?,
    val walkies: List<User>,
)
