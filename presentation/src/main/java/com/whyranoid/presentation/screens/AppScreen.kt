package com.whyranoid.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.whyranoid.domain.model.post.Post
import com.whyranoid.presentation.reusable.CircleProgressWithText
import com.whyranoid.presentation.screens.Screen.Companion.bottomNavigationItems
import com.whyranoid.presentation.screens.challenge.ChallengeCompleteScreen
import com.whyranoid.presentation.screens.challenge.ChallengeDetailScreen
import com.whyranoid.presentation.screens.challenge.ChallengeExitScreen
import com.whyranoid.presentation.screens.challenge.ChallengeMainScreen
import com.whyranoid.presentation.screens.community.CommentScreen
import com.whyranoid.presentation.screens.community.SearchFriendScreen
import com.whyranoid.presentation.screens.community.UserPostScreen
import com.whyranoid.presentation.screens.mypage.MyPageScreen
import com.whyranoid.presentation.screens.mypage.TotalBadgeScreen
import com.whyranoid.presentation.screens.mypage.UserPageScreen
import com.whyranoid.presentation.screens.mypage.addpost.AddPostScreen
import com.whyranoid.presentation.screens.mypage.editprofile.EditProfileScreen
import com.whyranoid.presentation.screens.mypage.following.FollowingScreen
import com.whyranoid.presentation.screens.running.RunningScreen
import com.whyranoid.presentation.screens.setting.SettingsScreen
import com.whyranoid.presentation.screens.signin.SignInScreen
import com.whyranoid.presentation.screens.splash.SplashScreen
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.CustomDialog
import com.whyranoid.presentation.util.ApiResponseDialog
import com.whyranoid.presentation.viewmodel.SplashState
import com.whyranoid.presentation.viewmodel.SplashViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppScreen(startWorker: () -> Unit) {
    val navController = rememberNavController()
    val splashViewModel = koinViewModel<SplashViewModel>()

    val splashState = splashViewModel.splashState.collectAsStateWithLifecycle()

    when (splashState.value) {
        SplashState.InitialState -> SplashScreen(isDay = splashViewModel.isDay)
        SplashState.SignInState -> SignInScreen(isDay = splashViewModel.isDay) { splashViewModel.finishSignIn() }
        SplashState.SignedInState -> AppScreenContent(startWorker, navController)
    }

    LaunchedEffect(Unit) {
        splashViewModel.splashStart()
    }
}

@Composable
fun AppScreenContent(
    startWorker: () -> Unit,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            if (currentDestination?.route in bottomNavigationItems.map { it.route }) {
                BottomNavigation(
                    modifier = Modifier.height(60.dp),
                    backgroundColor = Color.White,
                    elevation = 5.dp,
                ) {
                    bottomNavigationItems.forEach { screen ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    ImageVector.vectorResource(requireNotNull(if (selected) screen.iconSelected else screen.icon)),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (selected) WalkieColor.Primary else WalkieColor.GrayDefault,
                                )
                            },
                            label = {
                                Text(
                                    style = WalkieTypography.Caption,
                                    text = stringResource(requireNotNull(screen.resourceId)),
                                    color = WalkieColor.GrayBorder,
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            selectedContentColor = WalkieColor.Primary,
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Running.route,
            Modifier.padding(innerPadding),
        ) {
            composable(Screen.Running.route) {
                RunningScreen(navController, startWorker)
            }
            composable(Screen.Community.route) {
                CommunityScreen(navController)
            }
            composable(Screen.SearchFriendScreen.route) {
                SearchFriendScreen(navController)
            }
            composable(Screen.ChallengeMainScreen.route) {
                ChallengeMainScreen(navController)
            }

            // TODO: repo, viewModel로 하여금 uid를 받아오도록 변경 or MyPageScreen 내부적으로 uid 보관하도록 변경
            composable(Screen.MyPage.route) {
                MyPageScreen(navController)
            }

            composable(Screen.TotalBadgeScreen.route) {
                TotalBadgeScreen(navController)
            }

            composable(Screen.AddPostScreen.route) {
                AddPostScreen(navController = navController)
            }

            composable(Screen.EditProfileScreen.route) {
                EditProfileScreen(navController = navController)
            }

            composable(
                Screen.ChallengeDetailScreen.route,
                Screen.ChallengeDetailScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val challengeId = arguments.getLong("challengeId")
                val isChallenging = arguments.getBoolean("isChallenging")
                ChallengeDetailScreen(navController, challengeId, isChallenging)
            }

            composable(
                Screen.ChallengeExitScreen.route,
                Screen.ChallengeExitScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val challengeId = arguments.getLong("challengeId")
                ChallengeExitScreen(navController, challengeId)
            }

            composable(
                Screen.ChallengeCompleteScreen.route,
                Screen.ChallengeCompleteScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val challengeId = arguments.getLong("challengeId")
                ChallengeCompleteScreen(navController, challengeId)
            }

            composable(
                Screen.UserPageScreen.route,
                Screen.UserPageScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val uid = arguments.getLong(Screen.UID_ARGUMENT)
                val nickname = requireNotNull(arguments.getString(Screen.NICKNAME_ARGUMENT))
                val isFollowing = arguments.getBoolean(Screen.IS_FOLLOWING_ARGUMENT)
                UserPageScreen(navController, uid, nickname, isFollowing)
            }

            composable(
                Screen.FollowingScreen.route,
                Screen.FollowingScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val uid = arguments.getLong(Screen.UID_ARGUMENT)
                val pageNo = arguments.getInt(Screen.PAGE_NO)
                FollowingScreen(navController = navController, uid = uid, pageNo = pageNo)
            }

            composable(Screen.CommentScreen.route) {
                val post = navController.previousBackStackEntry?.savedStateHandle?.get<Post>("post")
                post?.let {
                    CommentScreen(
                        post = it,
                        onProfileClicked = { uid, nickname ->
                            navController.navigate("userpage/${uid}/${nickname}/false")
                        },
                        onMyProfileClicked = {
                            navController.navigate(Screen.MyPage.route)
                        },
                        onBackClicked = { navController.popBackStack() },
                    )
                }
            }

            composable(Screen.SettingScreen.route) {
                SettingsScreen(navHostController = navController)
            }

            composable(
                Screen.UserPostsScreen.route,
                Screen.UserPostsScreen.arguments,
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val uid = arguments.getLong(Screen.UID_ARGUMENT)
                val postId = arguments.getLong(Screen.POST_ID)
                UserPostScreen(
                    uid,
                    postId,
                    onProfileClicked = { user ->
                        navController.navigate("userPage/${user.uid}/${user.nickname}/false")
                    },
                    onCommentClicked = { post ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "post",
                            post
                        )
                        navController.navigate(Screen.CommentScreen.route)
                    }
                )
            }

            composable(
                route = Screen.WebViewScreen.route,
                arguments = Screen.WebViewScreen.arguments,
            ) {
                val url = it.arguments?.getString("url")
                WebViewScreen(url = url)
            }
        }

        val isLoading = ApiResponseDialog.isLoading.collectAsStateWithLifecycle()
        val isError = ApiResponseDialog.isShowError.collectAsStateWithLifecycle()
        if (isLoading.value) {
            CircleProgressWithText(text = "")
        } else if (isError.value) {
            CustomDialog(
                title = "네트워크 연결 실패",
                description = "네트워크 연결이 끊겼거나 속도가 느립니다.\n다시 시도해주세요.",
                onAction = { ApiResponseDialog.closeErrorDialog() },
                Modifier.clip(RoundedCornerShape(20.dp))
            )
        }
    }
}
