package com.whyranoid.data.datasource.running

import com.whyranoid.data.API
import com.whyranoid.data.model.account.NickProfileResponse
import com.whyranoid.data.model.running.LikeTotalResponse
import com.whyranoid.data.model.running.RunningFinishRequest
import com.whyranoid.data.model.running.RunningFinishResponse
import com.whyranoid.data.model.running.RunningStartRequest
import com.whyranoid.data.model.running.SendLikeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RunningService {
    @POST(API.WalkingControl.RUNNING_START)
    suspend fun runningStart(@Body runningStartRequest: RunningStartRequest): Response<Long>

    @POST(API.WalkingControl.RUNNING_FINISH)
    suspend fun runningFinish(@Body runningFinishRequest: RunningFinishRequest): Response<RunningFinishResponse>

    @POST(API.WalkingControl.SEND_LIKE)
    suspend fun sendLike(@Body sendLikeRequest: SendLikeRequest): Response<LikeTotalResponse>

    @GET(API.WalkingControl.LIKE_TOTAL)
    suspend fun getTotalLiker(
        @Query("walkieId") id: Long,
        @Query("authId") authId: String,
    ): Response<LikeTotalResponse>

    @GET(API.WalkingControl.LIKE_COUNT)
    suspend fun getLikeCount(
        @Query("walkieId") id: Long,
        @Query("authId") authId: String,
    ): Response<Long>
}
