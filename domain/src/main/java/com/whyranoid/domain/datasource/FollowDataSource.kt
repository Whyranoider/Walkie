package com.whyranoid.domain.datasource

import com.whyranoid.domain.model.user.User

interface FollowDataSource {
    suspend fun follow(followerId: Long, followedId: Long): Result<Long>

    suspend fun unfollow(followerId: Long, followedId: Long): Result<Long>

    suspend fun getWalkingFollowings(uid: Long): Result<List<User>>

    suspend fun getFollowings(uid: Long): Result<List<User>>

    suspend fun getFollowers(uid: Long): Result<List<User>>
}
