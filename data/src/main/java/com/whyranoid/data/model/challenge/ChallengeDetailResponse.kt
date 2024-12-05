package com.whyranoid.data.model.challenge

import com.whyranoid.domain.model.challenge.Challenge
import com.whyranoid.domain.model.challenge.ChallengeType
import com.whyranoid.domain.util.EMPTY

data class ChallengeDetailResponse(
    val challenge: ChallengeFromServer,
    val walkies: List<Walkie>
) {
    fun toChallenge(): Challenge {
        val challenge = Challenge(
            challenge.accCalories,
            challenge.accCount,
            challenge.accDistance,
            challenge.accTime,
            challenge.badge.toBadge(),
            challenge.calorie,
            ChallengeType.getChallengeTypeByString(challenge.category ?: String.EMPTY),
            challenge.challengeEdate,
            challenge.challengeId,
            challenge.challengeSdate,
            challenge.content,
            challenge.distance,
            challenge.endTime,
            challenge.goalCount,
            challenge.img,
            challenge.name,
            challenge.period,
            challenge.progress,
            challenge.startTime,
            challenge.status,
            challenge.timeLimit,
            challenge.time,
            walkies.map { it.toUser() }
        )
        return challenge
    }
}