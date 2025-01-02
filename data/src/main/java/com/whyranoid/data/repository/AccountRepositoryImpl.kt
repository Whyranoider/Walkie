package com.whyranoid.data.repository

import android.util.Log
import com.whyranoid.data.AccountDataStore
import com.whyranoid.domain.datasource.AccountDataSource
import com.whyranoid.domain.model.account.Sex
import com.whyranoid.domain.model.account.UserInfo
import com.whyranoid.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AccountRepositoryImpl(
    private val accountDataSource: AccountDataSource,
    private val accountDataStore: AccountDataStore,
) : AccountRepository {

    override val authId: Flow<String?> = accountDataStore.authId
    override val walkieId: Flow<Long?> = accountDataStore.uId
    override val userName: Flow<String?> = accountDataStore.userName
    override val nickName: Flow<String?> = accountDataStore.nickName
    override val profileUrl: Flow<String?> = accountDataStore.profileUrl

    override suspend fun getUID(): Long {
        return requireNotNull(walkieId.first())
    }

    override suspend fun signUp(
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
    ): Result<Long> {
        return kotlin.runCatching {
            accountDataSource.signUp(
                userName,
                nickName,
                profileUrl,
                authId,
                agreeGps,
                agreeSubscription
            )
                .onSuccess { uid ->
                    accountDataStore.updateUId(uid)
                    accountDataStore.updateAuthId(authId)
                    accountDataStore.updateUserName(userName)
                    accountDataStore.updateNickName(nickName)
                    profileUrl?.let { url -> accountDataStore.updateProfileUrl(url) }
                    return@runCatching uid
                }
            return Result.failure(Exception("회원가입 실패"))
        }
    }

    override suspend fun signIn(authorId: String, name: String): Result<Long> {
        return kotlin.runCatching {
            accountDataSource.signIn(authorId).onSuccess {
                val (uid, nickname, profileImg) = it
                accountDataStore.updateUId(uid)
                accountDataStore.updateUserName(name)
                accountDataStore.updateNickName(nickname)
                accountDataStore.updateAuthId(authorId)
                profileImg?.let { url -> accountDataStore.updateProfileUrl(url) }
                return@runCatching it.walkieId
            }.onFailure {
                // Error handling
            }
            return Result.failure(Exception("로그인 실패"))
        }
    }

    override suspend fun singOut(): Result<Boolean> {
        return kotlin.runCatching {
            accountDataStore.removeAll()
            true
        }
    }

    override suspend fun checkNickName(nickName: String): Result<Pair<Boolean, String>> {
        return kotlin.runCatching {
            accountDataSource.nickCheck(nickName).onSuccess {
                return@runCatching it
            }.onFailure {
                Log.d("checkNickName", it.message.toString())
            }
            return Result.failure(Exception("중복 검사 실패"))
        }
    }

    override suspend fun changeMyInfo(
        walkieId: Long,
        nickName: String,
        profileUrl: String?
    ): Result<Boolean> {
        return kotlin.runCatching {
            accountDataSource.changeMyInfo(
                walkieId,
                nickName,
                profileUrl
            ).onSuccess {
                accountDataStore.updateProfileUrl(profileUrl ?: "")
                accountDataStore.updateNickName(nickName)
                return@runCatching it
            }
            return Result.failure(Exception("마이페이지 정보 수정 실패"))
        }
    }

    override suspend fun getUserInfo(walkieId: Long): Result<UserInfo> {
        return accountDataSource.getUserInfo(walkieId)
    }

    override suspend fun leave(walkieId: Long): Result<Unit> {
        return kotlin.runCatching {
            accountDataSource.leave(walkieId)
        }
    }
}
