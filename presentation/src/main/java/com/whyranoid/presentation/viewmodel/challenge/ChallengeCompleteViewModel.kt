package com.whyranoid.presentation.viewmodel.challenge

import androidx.lifecycle.ViewModel
import com.whyranoid.domain.model.challenge.Challenge
import com.whyranoid.domain.usecase.GetChallengeDetailUseCase
import com.whyranoid.presentation.model.UiState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

sealed class ChallengeCompleteSideEffect {

}

data class ChallengeCompleteState(
    val challenge: UiState<Challenge> = UiState.Idle,
)

class ChallengeCompleteViewModel(
    private val getChallengeDetailUseCase: GetChallengeDetailUseCase
) : ViewModel(), ContainerHost<ChallengeCompleteState, ChallengeCompleteSideEffect> {

    override val container =
        container<ChallengeCompleteState, ChallengeCompleteSideEffect>(ChallengeCompleteState())

    fun getChallengeDetail(challengeId: Long) = intent {
        reduce {
            state.copy(challenge = UiState.Loading)
        }

        val challenge = getChallengeDetailUseCase(challengeId)

        reduce {
            state.copy(challenge = UiState.Success(challenge))
        }
    }
}