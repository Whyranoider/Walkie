package com.whyranoid.presentation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.running.RunningData
import com.whyranoid.domain.model.running.RunningHistory
import com.whyranoid.domain.model.running.RunningPosition
import com.whyranoid.domain.model.running.UserLocation
import com.whyranoid.domain.model.user.User
import com.whyranoid.domain.repository.RunningHistoryRepository
import com.whyranoid.domain.repository.RunningRepository
import com.whyranoid.domain.usecase.running.GetRunningFollowerUseCase
import com.whyranoid.domain.usecase.running.RunningFinishUseCase
import com.whyranoid.domain.usecase.running.RunningStartUseCase
import com.whyranoid.domain.usecase.running.SendLikeUseCase
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.model.running.RunningFollower
import com.whyranoid.presentation.model.running.RunningInfo
import com.whyranoid.presentation.model.running.SavingState
import com.whyranoid.presentation.model.running.TrackingMode
import com.whyranoid.runningdata.RunningDataManager
import com.whyranoid.runningdata.model.RunningFinishData
import com.whyranoid.runningdata.model.RunningState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

sealed class RunningScreenSideEffect

data class RunningScreenState(
    val runningState: UiState<RunningState> = UiState.Idle,
    val runningFollowerState: UiState<Pair<List<RunningFollower>, List<User>>> = UiState.Idle,
    val likedCountState: UiState<Int> = UiState.Idle,
    val runningInfoState: UiState<RunningInfo> = UiState.Idle,
    val runningResultInfoState: UiState<RunningInfo> = UiState.Idle,
    val trackingModeState: UiState<TrackingMode> = UiState.Idle,
    val runningFinishState: UiState<RunningFinishData> = UiState.Idle,
    val userLocationState: UiState<UserLocation> = UiState.Idle,
    val editState: UiState<Boolean> = UiState.Idle,
    val selectedImage: UiState<Uri> = UiState.Idle,
    val savingState: UiState<SavingState> = UiState.Idle,
)

class RunningViewModel(
    val runningStartUseCase: RunningStartUseCase,
    val runningFinishUseCase: RunningFinishUseCase,
    val getRunningFollowerUseCase: GetRunningFollowerUseCase,
    val sendLikeUseCase: SendLikeUseCase,
    private val runningRepository: RunningRepository,
    private val runningHistoryRepository: RunningHistoryRepository,
) : ViewModel(), ContainerHost<RunningScreenState, RunningScreenSideEffect> {

    private var savedHistory: List<List<com.whyranoid.runningdata.model.RunningPosition>>? = null

    private val runningDataManager = RunningDataManager.getInstance()
    var startWorker: (() -> Unit)? = null

    override val container = container<RunningScreenState, RunningScreenSideEffect>(
        RunningScreenState(),
    )

    fun sendLike(receiverId: Long) {
        viewModelScope.launch {
            sendLikeUseCase(receiverId).onSuccess {
                intent {
                    val lists =
                        state.runningFollowerState.getDataOrNull() ?: Pair(listOf(), listOf())
                    reduce {
                        state.copy(
                            runningFollowerState = UiState.Success(
                                Pair(
                                    lists.first.map { it.copy(isLiked = it.user.uid == receiverId) },
                                    lists.second,
                                ),
                            ),
                        )
                    }
                }
            }
        }
    }

    fun getRunningFollowingsState() {
        viewModelScope.launch {
            getRunningFollowerUseCase().distinctUntilChanged().collect {
                intent {
                    val likedFollowings =
                        state.runningFollowerState.getDataOrNull()?.first?.associateBy(
                            { it.user.uid },
                            { it.isLiked },
                        )
                    reduce {
                        state.copy(
                            runningFollowerState = UiState.Success(
                                Pair(
                                    it.first.map { u ->
                                        RunningFollower(
                                            u,
                                            likedFollowings?.get(u.uid) ?: false,
                                        )
                                    },
                                    it.second,
                                ),
                            ),
                        )
                    }
                }
            }
        }
    }

    fun getRunningState() {
        runningRepository.listenLocation()
        viewModelScope.launch {
            runningRepository.userLocationState.collect {
                intent {
                    reduce {
                        state.copy(userLocationState = UiState.Success(it))
                    }
                }
            }
        }
        viewModelScope.launch {
            runningDataManager.runningState.collect { runningState ->
                intent {
                    reduce {
                        val runningInfo =
                            runningState.runningData.toWalikeRunningData().toRunningInfo()
                        state.copy(
                            runningState = UiState.Success(runningState),
                            runningInfoState = UiState.Success(runningInfo),
                            trackingModeState = UiState.Success(
                                state.trackingModeState.getDataOrNull() ?: TrackingMode.FOLLOW,
                            ),
                        )
                    }
                }
            }
        }
    }

    fun startRunning() {
        viewModelScope.launch {
            runningStartUseCase().onSuccess {
                startWorker?.invoke()
                runningRepository.removeListener()
                intent {
                    reduce {
                        state.copy(trackingModeState = UiState.Success(TrackingMode.FOLLOW))
                    }
                }
            }.onFailure {
                Log.d("startRunning Failure", it.message.toString())
            }
        }
    }

    fun pauseRunning() {
        runningDataManager.pauseRunning()
    }

    fun resumeRunning() {
        runningDataManager.resumeRunning().onSuccess {
            intent {
                reduce {
                    state.copy(trackingModeState = UiState.Success(TrackingMode.FOLLOW))
                }
            }
        }
    }

    fun finishRunning() {
        viewModelScope.launch {
            runningFinishUseCase().onSuccess {
                intent {
                    state.runningInfoState.getDataOrNull()?.let {
                        reduce {
                            state.copy(runningResultInfoState = UiState.Success(it))
                        }
                    }
                }
                runningDataManager.finishRunning().onSuccess { runningFinishData ->
                    intent {
                        reduce {
                            state.copy(runningFinishState = UiState.Success(runningFinishData))
                        }
                    }
                }
            }.onFailure {
                Log.d("finishRunning Failure", it.message.toString())
            }
        }
    }

    fun onTrackingButtonClicked() {
        intent {
            reduce {
                state.copy(
                    trackingModeState = UiState.Success(
                        when (state.trackingModeState.getDataOrNull()) {
                            TrackingMode.NONE -> {
                                TrackingMode.NO_FOLLOW
                            }
                            TrackingMode.NO_FOLLOW -> {
                                TrackingMode.FOLLOW
                            }
                            TrackingMode.FOLLOW -> {
                                TrackingMode.NONE
                            }
                            else -> {
                                TrackingMode.FOLLOW
                            }
                        },
                    ),
                )
            }
        }
    }

    fun onTrackingCanceledByGesture() {
        intent {
            reduce {
                state.copy(
                    trackingModeState = UiState.Success(TrackingMode.NONE),
                )
            }
        }
    }

    fun openEdit() {
        intent {
            reduce {
                state.copy(editState = UiState.Success(true))
            }
        }
    }

    fun closeEdit() {
        intent {
            reduce {
                state.copy(editState = UiState.Success(false))
            }
        }
    }

    fun selectImage(uri: Uri) {
        intent {
            reduce {
                state.copy(selectedImage = UiState.Success(uri))
            }
        }
    }

    fun removeImage() = intent {
        reduce {
            state.copy(selectedImage = UiState.Idle)
        }
    }

    fun saveHistory(bitmap: Bitmap, finishData: RunningFinishData) {
        if (savedHistory == finishData.runningPositionList) return
        savedHistory = finishData.runningPositionList
        intent {
            runningHistoryRepository.saveRunningHistory(
                RunningHistory(
                    0L,
                    RunningData(
                        finishData.runningHistory.totalDistance,
                        finishData.runningHistory.pace,
                        finishData.runningHistory.totalRunningTime,
                        (finishData.runningHistory.totalDistance * 0.07).toInt(),
                        (finishData.runningHistory.totalDistance * 1.312).toInt(),
                        finishData.runningPositionList.map { list ->
                            list.map { runningPosition ->
                                RunningPosition(
                                    runningPosition.latitude,
                                    runningPosition.longitude,
                                )
                            }
                        },
                    ),
                    System.currentTimeMillis(),
                    state.selectedImage.getDataOrNull()?.let { it.toString() },
                ),
            ).onSuccess {
                reduce {
                    state.copy(
                        savingState = UiState.Idle,
                        runningFinishState = UiState.Idle,
                        selectedImage = UiState.Idle,
                        editState = UiState.Idle,
                        runningResultInfoState = UiState.Idle,
                    )
                }
                runningRepository.listenLocation()
            }
        }
    }

    fun takeSnapShot(runningFinishData: RunningFinishData) {
        intent {
            reduce {
                state.copy(savingState = UiState.Success(SavingState.Start(runningFinishData)))
            }
        }
    }

    companion object {
        const val MAP_MAX_ZOOM = 18.0
        const val MAP_MIN_ZOOM = 10.0
    }
}

fun MogakRunningData.toWalikeRunningData(): WalkieRunningData {
    return WalkieRunningData(
        distance = this.totalDistance,
        pace = this.pace,
        totalRunningTime = this.runningTime,
        calories = (this.totalDistance * 0.07).toInt(),
        steps = (this.totalDistance * 1.312).toInt(),
        paths = this.runningPositionList.map { list ->
            list.map { runningPosition ->
                RunningPosition(
                    runningPosition.longitude,
                    runningPosition.latitude,
                )
            }
        },
    )
}

fun WalkieRunningData.toRunningInfo(): RunningInfo {
    return RunningInfo(
        distance = this.distance,
        pace = this.pace,
        runningTime = this.totalRunningTime,
        calories = this.calories.toDouble(),
        steps = this.steps,
    )
}

typealias MogakRunningData = com.whyranoid.runningdata.model.RunningData
typealias WalkieRunningData = com.whyranoid.domain.model.running.RunningData
