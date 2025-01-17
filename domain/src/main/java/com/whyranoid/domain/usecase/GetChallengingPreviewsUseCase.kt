package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.challenge.ChallengePreview
import com.whyranoid.domain.repository.ChallengeRepository

class GetChallengingPreviewsUseCase(
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke(uid: Int) : Result<List<ChallengePreview>> {
        return challengeRepository.getChallengingPreviews(uid)
    }
}