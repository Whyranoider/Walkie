package com.whyranoid.domain.repository

import com.whyranoid.domain.model.account.Sex
import com.whyranoid.domain.model.account.UserInfo
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val authId: Flow<String?>
    val walkieId: Flow<Long?>
    val userName: Flow<String?>
    val nickName: Flow<String?>
    val profileUrl: Flow<String?>

    suspend fun signUp(
        authId: String,
        userName: String,
        profileUrl: String?,
        nickName: String,
        birthDay: String,
        phoneNumber: String,
        sex: Sex,
        height: Int,
        weight: Int,
        agreeGps: Boolean,
        agreeSubscription: Boolean,
    ): Result<Long>

    suspend fun getUID(): Long

    suspend fun signIn(authorId: String, name: String): Result<Long>

    suspend fun singOut(): Result<Boolean>
    suspend fun checkNickName(nickName: String): Result<Pair<Boolean, String>>

    suspend fun changeMyInfo(walkieId: Long, nickName: String, profileUrl: String?): Result<Boolean>

    suspend fun getUserInfo(walkieId: Long): Result<UserInfo>

    suspend fun leave(walkieId: Long): Result<Unit>
}
