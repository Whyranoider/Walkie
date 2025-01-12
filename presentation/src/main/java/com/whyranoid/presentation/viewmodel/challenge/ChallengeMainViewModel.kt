package com.whyranoid.presentation.viewmodel.challenge

import androidx.lifecycle.ViewModel
import com.whyranoid.domain.model.challenge.ChallengePreview
import com.whyranoid.domain.model.challenge.ChallengeType
import com.whyranoid.domain.usecase.GetChallengePreviewsByTypeUseCase
import com.whyranoid.domain.usecase.GetChallengingPreviewsUseCase
import com.whyranoid.domain.usecase.GetMyUidUseCase
import com.whyranoid.domain.usecase.GetNewChallengePreviewsUseCase
import com.whyranoid.domain.usecase.GetTopRankChallengePreviewsUseCase
import com.whyranoid.presentation.model.UiState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

sealed class ChallengeMainSideEffect {

}

data class ChallengeMainState(
    val uid: UiState<Int> = UiState.Idle,
    val newChallengePreviewsState: UiState<List<ChallengePreview>> = UiState.Idle,
    val challengingPreviewsState: UiState<List<ChallengePreview>> = UiState.Idle,
    val typedChallengePreviewsState: UiState<List<List<ChallengePreview>>> = UiState.Idle,
    val topRankChallengePreviewState: UiState<List<ChallengePreview>> = UiState.Idle,
)

class ChallengeMainViewModel(
    private val getNewChallengePreviewsUseCase: GetNewChallengePreviewsUseCase,
    private val getChallengingPreviewsUseCase: GetChallengingPreviewsUseCase,
    private val getChallengePreviewsByTypeUseCase: GetChallengePreviewsByTypeUseCase,
    private val getTopRankChallengePreviewsUseCase: GetTopRankChallengePreviewsUseCase,
    private val getMyUidUseCase: GetMyUidUseCase
) : ViewModel(), ContainerHost<ChallengeMainState, ChallengeMainSideEffect> {

    override val container =
        container<ChallengeMainState, ChallengeMainSideEffect>(ChallengeMainState())

    init {
        intent {
            getMyUidUseCase().onSuccess { uid ->
                reduce {
                    state.copy(uid = UiState.Success(uid.toInt()))
                }
            }
        }
        getNewChallengeItems()
        getChallengingItems()
        getTypedChallengeItems()
        getTopRankChallengeItems()
    }

    fun getNewChallengeItems() = intent {
        reduce {
            state.copy(newChallengePreviewsState = UiState.Loading)
        }

        getNewChallengePreviewsUseCase(
            state.uid.getDataOrNull() ?: 0
        ).onSuccess { newChallengePreviews ->
            reduce {
                state.copy(
                    newChallengePreviewsState = UiState.Success(newChallengePreviews)
                )
            }
        }.onFailure {

        }

    }

    fun getChallengingItems() = intent {
        reduce {
            state.copy(challengingPreviewsState = UiState.Loading)
        }
        getChallengingPreviewsUseCase(
            state.uid.getDataOrNull() ?: 0
        ).onSuccess { challengingPreviews ->
            reduce {
                state.copy(
                    challengingPreviewsState = UiState.Success(challengingPreviews)
                )
            }
        }.onFailure {

        }

    }

    fun getTopRankChallengeItems() = intent {
        reduce {
            state.copy(typedChallengePreviewsState = UiState.Loading)
        }
        getTopRankChallengePreviewsUseCase().onSuccess { topRankChallengeItem ->
            reduce {
                state.copy(
                    topRankChallengePreviewState = UiState.Success(topRankChallengeItem)
                )
            }
        }.onFailure {

        }
    }

    fun getTypedChallengeItems() = intent {
        reduce {
            state.copy(typedChallengePreviewsState = UiState.Loading)
        }
        val typedChallengePreviews =
            MutableList(ChallengeType.values().size) { emptyList<ChallengePreview>() }
        ChallengeType.values().forEachIndexed { index, challengeType ->
            typedChallengePreviews[index] = getChallengePreviewsByTypeUseCase(challengeType)
        }
        reduce {
            state.copy(
                typedChallengePreviewsState = UiState.Success(typedChallengePreviews)
            )
        }
    }

}