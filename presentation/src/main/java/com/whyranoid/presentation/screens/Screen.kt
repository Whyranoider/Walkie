package com.whyranoid.presentation.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.whyranoid.presentation.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int? = null,
    @DrawableRes val icon: Int? = null,
    @DrawableRes val iconSelected: Int? = null,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    object Running : Screen(
        "running",
        R.string.running,
        R.drawable.ic_running_screen,
        R.drawable.ic_running_screen_selected,
    )

    object Community : Screen(
        "community",
        R.string.community,
        R.drawable.ic_community_screen,
        R.drawable.ic_community_screen_selected,
    )

    object SearchFriendScreen : Screen(
        "searchFriendScreen",
    )

    object ChallengeMainScreen :
        Screen(
            "challengeMain",
            R.string.challenge_main,
            R.drawable.ic_challenge_screen,
            R.drawable.ic_challenge_screen_selected,
        )

    object MyPage : Screen(
        "myPage",
        R.string.my_page,
        R.drawable.ic_mypage_screen,
        R.drawable.ic_mypage_screen_selected,
    )

    object TotalBadgeScreen: Screen(
        route = "totalBadgeScreen"
    )

    object AddPostScreen : Screen(
        route = "addPostScreen",
    )

    object EditProfileScreen : Screen(
        route = "editProfileScreen",
        arguments =
        listOf(
            navArgument("imageUrl") { type = NavType.StringType },
            navArgument("name") { type = NavType.StringType },
            navArgument("nickName") { type = NavType.StringType },
        ),
    ) {
        const val IMAGE_URL_KEY = "imageUrl"
        const val NAME_KEY = "name"
        const val NICKNAME_KEY = "nickName"
    }

    object ChallengeDetailScreen : Screen(
        route = "challengeDetail/{challengeId}/{isChallenging}",
        arguments =
        listOf(
            navArgument("challengeId") { type = NavType.LongType },
            navArgument("isChallenging") { type = NavType.BoolType },
        ),
    )

    object ChallengeExitScreen : Screen(
        route = "challengeExit/{challengeId}",
        arguments =
        listOf(
            navArgument("challengeId") { type = NavType.LongType },
        ),
    )

    object ChallengeCompleteScreen : Screen(
        route = "challengeComplete/{challengeId}",
        arguments =
        listOf(
            navArgument("challengeId") { type = NavType.LongType },
        ),
    )

    object UserPageScreen : Screen(
        route = "userPage/{$UID_ARGUMENT}/{$NICKNAME_ARGUMENT}/{$IS_FOLLOWING_ARGUMENT}",
        arguments =
        listOf(
            navArgument(UID_ARGUMENT) { type = NavType.LongType },
            navArgument(NICKNAME_ARGUMENT) { type = NavType.StringType },
            navArgument(IS_FOLLOWING_ARGUMENT) { type = NavType.BoolType },
        ),
    )

    object FollowingScreen : Screen(
        route = "followingScreen/{$UID_ARGUMENT}/{$PAGE_NO}",
        arguments =
        listOf(
            navArgument(UID_ARGUMENT) { type = NavType.LongType },
            navArgument(PAGE_NO) { type = NavType.IntType },
        ),
    ) {
        fun route(
            uid: Long,
            pageNo: Int,
        ): String {
            return "followingScreen/$uid/$pageNo"
        }
    }

    object CommentScreen : Screen(
        route = "commentScreen/{$POST_ID}",
        arguments =
        listOf(
            navArgument(POST_ID) { type = NavType.LongType },
        ),
    )

    object SettingScreen : Screen(
        "settingScreen",
    )

    object UserPostsScreen : Screen(
        route = "userPostsScreen/{$UID_ARGUMENT}/{$POST_ID}",
        arguments =
        listOf(
            navArgument(UID_ARGUMENT) { type = NavType.LongType },
            navArgument(POST_ID) { type = NavType.LongType },
        )
    ) {
        fun route(uid: Long, postId: Long) = "userPostsScreen/$uid/$postId"
    }

    object WebViewScreen : Screen(
        route = "webViewScreen/{$URL}",
        arguments = listOf(
            navArgument(URL) {
                type = NavType.StringType
                nullable = true
            }
        ),
    ) {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return route.replace("{$URL}", encodedUrl)
        }
    }

    companion object {
        val bottomNavigationItems = listOf(Running, Community, ChallengeMainScreen, MyPage)

        const val UID_ARGUMENT = "uid"
        const val NICKNAME_ARGUMENT = "nickname"
        const val IS_FOLLOWING_ARGUMENT = "isFollowing"
        const val PAGE_NO = "pageNo"
        const val POST_ID = "postId"
        const val URL = "url"
    }
}
