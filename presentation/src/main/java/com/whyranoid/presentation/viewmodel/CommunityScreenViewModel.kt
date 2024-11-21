package com.whyranoid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whyranoid.domain.model.post.Post
import com.whyranoid.domain.model.user.User
import com.whyranoid.domain.usecase.GetFollowingsPostsUseCase
import com.whyranoid.domain.usecase.GetMyFollowingUseCase
import com.whyranoid.domain.usecase.LikePostUseCase
import com.whyranoid.domain.usecase.running.GetRunningFollowerUseCase
import com.whyranoid.domain.usecase.running.SendLikeUseCase
import com.whyranoid.presentation.model.UiState
import com.whyranoid.presentation.model.running.RunningFollower
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

sealed interface CommunityScreenSideEffect

data class CommunityScreenState(
    val posts: UiState<List<Post>> = UiState.Idle,
    val following: UiState<List<User>> = UiState.Idle,
    val isEveryPost: UiState<Boolean> = UiState.Success(true),
    val runningFollowerState: UiState<Pair<List<RunningFollower>, List<User>>> = UiState.Idle,
)

class CommunityScreenViewModel(
    private val getMyFollowingUseCase: GetMyFollowingUseCase,
    private val getFollowingsPostsUseCase: GetFollowingsPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val sendLikeUseCase: SendLikeUseCase,
    private val getRunningFollowerUseCase: GetRunningFollowerUseCase,
) : ViewModel(), ContainerHost<CommunityScreenState, CommunityScreenSideEffect> {

    override val container =
        container<CommunityScreenState, CommunityScreenSideEffect>(CommunityScreenState())

    init {
        intent {
            val result = getMyFollowingUseCase()
            result.onSuccess { myFollowing ->
                reduce {
                    state.copy(
                        following = UiState.Success(myFollowing),
                    )
                }
            }
        }
        getPosts()
    }

    fun switchPostType() = intent {
        reduce {
            state.copy(isEveryPost = UiState.Success(state.isEveryPost.getDataOrNull()!!.not()))
        }
        getPosts()
    }

    fun getPosts() = intent {
        reduce {
            state.copy(posts = UiState.Loading)
        }
        val isEveryPost = state.isEveryPost.getDataOrNull() ?: true
        val result = getFollowingsPostsUseCase(isEveryPost)
        result.onSuccess { posts ->
            reduce {
                state.copy(
                    posts = UiState.Success(
                        (state.posts.getDataOrNull() ?: emptyList()) +
                                posts,
                    ),
                )
            }
        }
    }

    fun likePost(postId: Long) = intent {
        val result = likePostUseCase(postId)

        result.onSuccess { updatedLikeCount ->

            reduce {
                state.copy(
                    posts = UiState.Success(
                        state.posts.getDataOrNull()?.map {
                            if (it.id == postId) {
                                it.copy(
                                    likeCount = if (updatedLikeCount == -1L) it.likeCount - 1 else updatedLikeCount.toInt(),
                                    isLiked = it.isLiked.not(),
                                )
                            } else {
                                it
                            }
                        } ?: emptyList(),
                    ),
                )
            }
        }.onFailure {
            // TODO: Error handling
        }
    }

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
}
