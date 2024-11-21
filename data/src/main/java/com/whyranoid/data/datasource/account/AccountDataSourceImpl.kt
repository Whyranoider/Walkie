package com.whyranoid.data.datasource.account

import com.whyranoid.data.getResult
import com.whyranoid.data.model.account.SignUpRequest
import com.whyranoid.data.model.account.toLoginData
import com.whyranoid.data.model.account.toUserInfo
import com.whyranoid.domain.datasource.AccountDataSource
import com.whyranoid.domain.model.account.LoginData
import com.whyranoid.domain.model.account.UserInfo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AccountDataSourceImpl(private val accountService: AccountService) : AccountDataSource {
    override suspend fun signUp(
        name: String,
        nickName: String,
        profileUrl: String?,
        authId: String,
        agreeGps: Boolean,
        agreeMarketing: Boolean,
    ): Result<Long> {
        return kotlin.runCatching {
            val request = SignUpRequest(
                userName = nickName,
                name = name,
                profileImg = profileUrl ?: "",
                authId = authId,
                agreeGps = agreeGps,
                agreeSubscription = agreeMarketing,
            )

            val response = accountService.signUp(request)

            if (response.isSuccessful.not()) {
                throw Exception(response.errorBody().toString())
            } else if (response.body() == null) throw Exception(response.message())
            requireNotNull(response.body()?.walkieId?.toLong() ?: throw Exception("empty response"))
        }
    }

    override suspend fun nickCheck(nickName: String): Result<Pair<Boolean, String>> {
        return kotlin.runCatching {
            val response = accountService.checkNickName(nickName)
            if (response.isSuccessful.not()) {
                throw Exception(response.errorBody().toString())
            } else if (response.body() == null) throw Exception(response.message())
            requireNotNull(response.body()).let { Pair(it.isDuplicated, it.nickName ?: "empty") }
        }
    }

    override suspend fun signIn(authorId: String): Result<LoginData> {
        return kotlin.runCatching {
            val response = accountService.login(authorId)
            response.getResult {
                it.toLoginData()
            }
        }
    }

    override suspend fun changeMyInfo(walkieId: Long, nickName: String, profileUrl: String?): Result<Boolean> {
        return kotlin.runCatching {
            var imagePart: MultipartBody.Part? = null

            if (profileUrl != null) {
                val file = File(profileUrl)
                val fileBody = RequestBody.create(MediaType.parse("image/*"), file)
                imagePart = MultipartBody.Part.createFormData("profileImg", file.name, fileBody)
            }

            val response = accountService.changeMyInfo(
                walkieId,
                imagePart,
                nickName
            )
            if (response.isSuccessful) {
                return Result.success(true)
            } else {
                return Result.failure(Exception(response.message()))
            }
        }
    }

    override suspend fun getUserInfo(walkieId: Long): Result<UserInfo> {
        return kotlin.runCatching {
            accountService.getMyInfo(walkieId).getResult { it.toUserInfo() }
        }
    }

    override suspend fun leave(walkieId: Long): Result<Unit> {
        return kotlin.runCatching {
            accountService.leave(walkieId).getResult{ }
        }
    }
}
