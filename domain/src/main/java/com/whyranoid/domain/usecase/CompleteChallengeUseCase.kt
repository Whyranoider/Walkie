package com.whyranoid.domain.usecase

import com.whyranoid.domain.repository.ChallengeRepository
import javax.inject.Inject

class CompleteChallengeUseCase @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val getMyUidUseCase: GetMyUidUseCase
) {
    suspend operator fun invoke(challengeId: Int): Result<Unit> {
        val myId = getMyUidUseCase()
        return challengeRepository.changeChallengeStatus(challengeId,"C", myId.getOrNull()?.toInt() ?: -1)
    }

}