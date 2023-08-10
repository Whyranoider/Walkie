package com.whyranoid.data.datasource.account

import com.whyranoid.data.API
import com.whyranoid.data.model.account.NickCheckResponse
import com.whyranoid.data.model.account.SignUpResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountService {
    @GET(API.CHECK_NICKNAME)
    suspend fun checkNickName(
        @Query("userName") nickName: String,
        @Query("profileImg") profileUrl: String = "",
        @Query("authId") authId: String = "",
        @Query("agreeGps") agreeGps: Boolean = false,
        @Query("agreeSubscription") agreeSubscription: Boolean = false,
    ): Response<NickCheckResponse>

    @POST(API.SIGN_UP)
    suspend fun signUp(
        @Query("userName") nickName: String,
        @Query("profileImg") profileUrl: String,
        @Query("authId") authId: String,
        @Query("agreeGps") agreeGps: Boolean,
        @Query("agreeSubscription") agreeSubscription: Boolean,
    ): Response<SignUpResponse>
}
