package com.whyranoid.data.model.challenge

import com.whyranoid.domain.model.challenge.ChallengePreview
import com.whyranoid.domain.model.challenge.ChallengeType

data class ChallengeResponse(
    val calorie: Int,
    val category: String,
    val challengeId: Int,
    val distance: Int,
    val endTime: String,
    val goalCount: Int,
    val name: String,
    val newFlag: Int,
    val period: Int,
    val progress: Int,
    val startTime: String,
    val status: String,
    val timeLimit: Int
) {
    fun toChallengePreview(): ChallengePreview {
        return ChallengePreview(
            id = challengeId.toLong(),
            title = name,
            progress = progress.toFloat(),
            type = ChallengeType.getChallengeTypeByString(category)
        )
    }
}