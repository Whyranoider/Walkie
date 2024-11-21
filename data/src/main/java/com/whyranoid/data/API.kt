package com.whyranoid.data

object API {
    const val BASE_URL = "http://3.35.102.89:8080/"

    const val CHECK_NICKNAME = "api/walkies/signup/check"

    const val SIGN_UP = "api/walkies/signup"

    const val LEAVE = "api/walkies/leave"

    const val FOLLOW = "api/follow/follow"

    const val WALKING_FOLLOWING = "api/follow/{uid}/walking-followings"

    const val FOLLOWINGS = "api/follow/{uid}/following"

    const val FOLLOWERS = "api/follow/{uid}/follower"

    const val UNFOLLOW = "api/follow/unfollow"

    const val UPLOAD_POST = "api/community/upload-post"

    // 사용자가 작성한 게시글을 가져온다
    const val LIST_UP_MY_POST = "api/walkies/listup-my-post"

    // 사용자의 팔로잉들의 게시글을 가져온다
    const val LIST_UP_POST = "api/community/listup-post"

    const val LIST_UP_EVERY_POST = "api/community/listup-every-post"

    const val LIST_UP_COMMENT = "api/community/listup-comment"

    const val SEARCH = "api/community/search-nickname"

    const val SEND_LIKE = "api/community/send-like"

    const val WRITE_COMMENT = "api/community/write-comment"

    const val LOGIN = "api/walkies/login"

    const val NEW_CHALLENGE = "/api/challenge/challenges/new"

    const val PROGRESSING_CHALLENGE = "api/challenge/challenges/progress"

    const val TOP_RANK_CHALLENGE = "api/challenge/challenges/top-rank"

    const val CHALLENGE_CATEGORY = "api/challenge/challenges/category"

    const val CHALLENGE_DETAIL = "api/challenge/challenge-detail"

    const val CHALLENGE_START = "api/challenge/challenge-detail/start"

    const val CHALLENGE_CHANGE_STATUS = "api/challenge/challenge-detail/update-status"

    object WalkingControl {
        const val RUNNING_START = "api/walk/start"

        const val RUNNING_FINISH = "api/walk/save"

        const val SEND_LIKE = "api/walk/send-like"

        const val LIKE_TOTAL = "api/walk/count-total"

        const val LIKE_COUNT = "api/walk/count-like"

        const val MY = "/api/walkies/my"
    }

    object BadgeAPI {
        const val BADGES = "/api/badge/badges"

        const val OBTAIN_BADGE = "/api/badge/obtain-badges"

        const val UPDATE_BADGE_INDICES = "/api/badge/update-badge-indices"
    }
}
