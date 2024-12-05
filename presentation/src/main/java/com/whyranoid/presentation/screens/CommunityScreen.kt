package com.whyranoid.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.whyranoid.presentation.component.bar.WalkieTopBar
import com.whyranoid.presentation.component.community.PostItem
import com.whyranoid.presentation.component.community.RunningFollowerItem
import com.whyranoid.presentation.component.running.RunningFollowerItemWithLikable
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.CommunityScreenViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommunityScreen(navController: NavController) {
    val viewModel = koinViewModel<CommunityScreenViewModel>()
    val state by viewModel.collectAsState()

    LaunchedEffect(LocalLifecycleOwner.current) {
        viewModel.getRunningFollowingsState()
    }

    Scaffold(
        topBar = {
            WalkieTopBar(
                leftContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "커뮤니티",
                            style = WalkieTypography.Title.copy(fontWeight = FontWeight(600)),
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        val isEveryPost = state.isEveryPost.getDataOrNull() ?: true

                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isEveryPost) WalkieColor.GrayBackground else WalkieColor.PrimarySurface)
                                .clickable { viewModel.switchPostType() },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "팔로잉",
                                fontSize = 14.sp,
                                color = if (isEveryPost) WalkieColor.GrayBorder else WalkieColor.Primary,
                                fontWeight = if (isEveryPost) FontWeight.Thin else FontWeight.SemiBold,
                                letterSpacing = if (isEveryPost) 0.sp else (-0.7).sp
                            )
                        }
                    }
                },
                rightContent = {
                    Row {
                        Icon(
                            modifier =
                            Modifier
                                .clickable {
                                    navController.navigate(Screen.AddPostScreen.route)
                                },
                            imageVector = Icons.Filled.Add,
                            contentDescription = "추가 버튼",
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(
                            modifier =
                            Modifier
                                .clickable {
                                    navController.navigate(Screen.SearchFriendScreen.route)
                                },
                            imageVector = Icons.Filled.Search,
                            contentDescription = "검색 버튼",
                        )
                    }
                },
            )
        },
    ) {

        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyRow {
                state.myThumbnailState.getDataOrNull()?.let { myInfo ->
                    item {
                        RunningFollowerItem(
                            user = myInfo,
                            onClickProfile = {
                                navController.navigate(Screen.MyPage.route)
                            },
                        )
                    }
                }
                state.runningFollowerState.getDataOrNull()?.let { (running, notRunning) ->
                    items(running.size) {
                        RunningFollowerItemWithLikable(
                            user = running[it].user,
                            onClickProfile = { user ->
                                navController.navigate("userPage/${user.uid}/${user.nickname}/${true}")
                            },
                            onClick = viewModel::sendLike,
                            circleBorderColor = WalkieColor.Primary,
                            isLiked = running[it].isLiked,
                        )
                    }
                    items(notRunning.size) {
                        RunningFollowerItemWithLikable(
                            user = notRunning[it],
                            circleBorderColor = WalkieColor.GrayBorder,
                        )
                    }
                }
            }

            val refreshing = state.posts.getDataOrNull() == null

            val pullRefreshState = rememberPullRefreshState(
                refreshing = refreshing,
                onRefresh = {
                    viewModel.getPosts()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn {
                    state.posts.getDataOrNull()?.forEach { post ->
                        item {
                            PostItem(
                                post = post,
                                onLikeClicked = { postId ->
                                    viewModel.likePost(postId)
                                },
                                onProfileClicked = { user ->
                                    state.following.getDataOrNull()?.let { followings ->
                                        val isFollowing = followings.contains(user)
                                        navController.navigate("userPage/${user.uid}/${user.nickname}/$isFollowing")
                                    }
                                },
                                onCommentClicked = { post ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "post",
                                        post
                                    )
                                    navController.navigate(Screen.CommentScreen.route)
                                },
                                onPostPreviewClicked = { uid: Long, postId: Long ->
                                    navController.navigate(Screen.UserPostsScreen.route(uid, postId))
                                }
                            )
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter)
                )
            }
        }
    }
}
