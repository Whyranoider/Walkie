package com.whyranoid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.challenge.Badge
import com.whyranoid.domain.model.post.PostPreview
import com.whyranoid.domain.model.user.UserDetail
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.usecase.GetPostUseCase
import com.whyranoid.domain.usecase.GetUserBadgesUseCase
import com.whyranoid.domain.usecase.GetUserDetailUseCase
import com.whyranoid.domain.usecase.GetUserPostPreviewsUseCase
import com.whyranoid.domain.usecase.SignOutUseCase
import com.whyranoid.presentation.model.UiState
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.util.Date

sealed class UserPageSideEffect

data class UserPageState(
    val userDetailState: UiState<UserDetail> = UiState.Idle,
    val userBadgesState: UiState<List<Badge>> = UiState.Idle,
    val userPostPreviewsState: UiState<List<PostPreview>> = UiState.Idle,
    val calendarPreviewsState: UiState<List<PostPreview>> = UiState.Idle,
)

class UserPageViewModel(
    val accountRepository: AccountRepository,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getUserBadgesUseCase: GetUserBadgesUseCase,
    private val getUserPostPreviewsUseCase: GetUserPostPreviewsUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel(), ContainerHost<UserPageState, UserPageSideEffect> {

    override val container = container<UserPageState, UserPageSideEffect>(UserPageState())

    fun getUserDetail(uid: Long) = intent {
        reduce {
            state.copy(userDetailState = UiState.Loading)
        }
        getUserDetailUseCase(uid).onSuccess { userDetail ->
            reduce {
                state.copy(
                    userDetailState = UiState.Success(userDetail),
                )
            }
        }.onFailure {
            reduce {
                state.copy(userDetailState = UiState.Error(it.message.toString()))
            }
        }
    }

    fun getUserBadges(uid: Long) = intent {
        reduce {
            state.copy(userBadgesState = UiState.Loading)
        }
        getUserBadgesUseCase(uid).onSuccess { userBadges ->
            reduce {
                state.copy(
                    userBadgesState = UiState.Success(userBadges),
                )
            }
        }.onFailure {
            reduce {
                state.copy(userBadgesState = UiState.Error(it.message.toString()))
            }
        }
    }

    fun getUserPostPreviews(uid: Long) = intent {
        reduce {
            state.copy(userPostPreviewsState = UiState.Loading)
        }
        getUserPostPreviewsUseCase(uid).onSuccess { userPostPreviews ->
            reduce {
                state.copy(
                    userPostPreviewsState = UiState.Success(userPostPreviews),
                )
            }
        }.onFailure {
            reduce {
                state.copy(userPostPreviewsState = UiState.Error(it.message.toString()))
            }
        }
    }

    fun selectDate(localDate: LocalDate) = intent {
        reduce {
            state.copy(calendarPreviewsState = UiState.Loading)
        }
        reduce {
            val posts = state.userPostPreviewsState.getDataOrNull() ?: emptyList()
            val filtered = posts.filter {
                val date = Date(it.date)
                date.year + 1900 == localDate.year && date.month + 1 == localDate.monthValue && date.date == localDate.dayOfMonth
            }
            state.copy(calendarPreviewsState = UiState.Success(filtered))
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}
