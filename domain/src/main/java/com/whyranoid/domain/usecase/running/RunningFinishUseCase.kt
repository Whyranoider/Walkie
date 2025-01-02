package com.whyranoid.domain.usecase.running

import com.whyranoid.domain.model.running.CompletedRunning
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.RunningRepository
import kotlinx.coroutines.flow.first

class RunningFinishUseCase(
    private val accountRepository: AccountRepository,
    private val runningRepository: RunningRepository,
) {
    suspend operator fun invoke(
        historyId: Int,
        endTime: String,
        totalTime: Int,
        distance: Double,
        calorie: Int,
        step: Int
    ): Result<List<CompletedRunning>> {
        val uid = accountRepository.walkieId.first()
        val authId = accountRepository.authId.first()

        if (uid != null && authId != null) {

            return runningRepository.finishRunning(uid,
                authId,
                historyId,
                endTime,
                totalTime,
                distance,
                calorie,
                step
            )
        }
        return Result.failure(Exception("ID 정보 없음"))
    }
}
