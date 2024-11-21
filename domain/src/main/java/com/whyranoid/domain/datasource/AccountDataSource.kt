package com.whyranoid.domain.datasource

import com.whyranoid.domain.model.account.LoginData
import com.whyranoid.domain.model.account.UserInfo

interface AccountDataSource {
    suspend fun signUp(
        name: String,
        nickName: String,
        profileUrl: String?,
        authId: String,
        agreeGps: Boolean,
        agreeMarketing: Boolean,
    ): Result<Long>

    suspend fun nickCheck(nickName: String): Result<Pair<Boolean, String>>

    suspend fun signIn(authorId: String): Result<LoginData>

    suspend fun changeMyInfo(walkieId: Long, nickName: String, profileUrl: String?): Result<Boolean>

    suspend fun getUserInfo(walkieId: Long): Result<UserInfo>

    suspend fun leave(walkieId: Long): Result<Unit>
}
