package com.whyranoid.data.model.running

import com.whyranoid.domain.model.running.CompletedRunning


data class RunningFinishResponse(
    val completedChallenges: List<RunningFinishChallengeResponse>,
    val failedChallenges: List<RunningFinishChallengeResponse>,
    val ongoingChallenges: List<RunningFinishChallengeResponse>
) {
    fun toCompletedRunning(): List<CompletedRunning> {
        return completedChallenges.map {
            CompletedRunning(
                calorie = it.calorie,
                category = it.category,
                challengeId = it.challengeId,
                distance = it.distance,
                endTime = it.endTime,
                goalCount = it.goalCount,
                limitPerDay = it.limitPerDay,
                name = it.name,
                newFlag = it.newFlag,
                period = it.period,
                progress = it.progress,
                startTime = it.startTime,
                status = it.status,
                time = it.time,
                timeLimit = it.timeLimit
            )
        }
    }
}