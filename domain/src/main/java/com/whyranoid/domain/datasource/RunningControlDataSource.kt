package com.whyranoid.domain.datasource

import com.whyranoid.domain.model.running.CompletedRunning
import com.whyranoid.domain.model.user.User

interface RunningControlDataSource {
    suspend fun runningStart(id: Long): Result<Long>

    suspend fun runningFinish(
        id: Long,
        authId: String,
        historyId: Int,
        endTime: String,
        totalTime: Int,
        distance: Double,
        calorie: Int,
        step: Int
    ): Result<List<CompletedRunning>>

    suspend fun sendLike(id: Long, receiverId: Long): Result<Long>

    suspend fun getTotalLiker(
        id: Long,
        authId: String,
    ): Result<List<User>>

    suspend fun getLikeCount(
        id: Long,
        authId: String,
    ): Result<Long>
}
