package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.challenge.ChallengePreview
import com.whyranoid.domain.repository.ChallengeRepository

class GetNewChallengePreviewsUseCase(
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke() : List<ChallengePreview> {
        return challengeRepository.getNewChallengePreviews()
    }
}