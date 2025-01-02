package com.whyranoid.data.datasource.running

import com.whyranoid.data.getResult
import com.whyranoid.data.model.running.RunningFinishRequest
import com.whyranoid.data.model.running.RunningStartRequest
import com.whyranoid.data.model.running.SendLikeRequest
import com.whyranoid.domain.datasource.RunningControlDataSource
import com.whyranoid.domain.model.running.CompletedRunning
import com.whyranoid.domain.model.user.User
import com.whyranoid.domain.util.DATE_FORMAT
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RunningControlDataSourceImpl(private val runningService: RunningService) :
    RunningControlDataSource {
    override suspend fun runningStart(id: Long): Result<Long> {
        return kotlin.runCatching {
            requireNotNull(
                runningService.runningStart(
                    RunningStartRequest(
                        walkieId = id,
                        startTime =
                        DateTimeFormatter.ofPattern(String.DATE_FORMAT).format(LocalDateTime.now()),
                    ),
                ).body(),
            )
        }
    }

    override suspend fun runningFinish(
        id: Long,
        authId: String,
        historyId: Int,
        endTime: String,
        totalTime: Int,
        distance: Double,
        calorie: Int,
        step: Int
    ): Result<List<CompletedRunning>> {
        return kotlin.runCatching {
            runningService.runningFinish(
                RunningFinishRequest(id,
                    authId,
                    historyId,
                    endTime,
                    totalTime,
                    distance,
                    calorie,
                    step),
            ).getResult {
                it.toCompletedRunning()
            }
        }
    }

    override suspend fun sendLike(id: Long, receiverId: Long): Result<Long> {
        return kotlin.runCatching {
            requireNotNull(
                runningService.sendLike(
                    SendLikeRequest(
                        senderId = id,
                        receiverId = receiverId,
                    ),
                ).body()?.likerProfiles?.size?.toLong(),
            )
        }
    }

    override suspend fun getTotalLiker(id: Long, authId: String): Result<List<User>> {
        return kotlin.runCatching {
            requireNotNull(
                runningService.getTotalLiker(id, authId).body()?.likerProfiles?.map { it.toUser() },
            )
        }
    }

    override suspend fun getLikeCount(id: Long, authId: String): Result<Long> {
        return kotlin.runCatching {
            requireNotNull(
                runningService.getLikeCount(id, authId).body(),
            )
        }
    }
}
