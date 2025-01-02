package com.whyranoid.data.repository

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.whyranoid.domain.datasource.RunningControlDataSource
import com.whyranoid.domain.model.running.CompletedRunning
import com.whyranoid.domain.model.running.UserLocation
import com.whyranoid.domain.repository.RunningRepository
import com.whyranoid.runningdata.RunningDataManager
import com.whyranoid.runningdata.model.RunningState
import kotlinx.coroutines.flow.MutableStateFlow

class RunningRepositoryImpl(
    context: Context,
    private val runningControlDataSource: RunningControlDataSource,
) : RunningRepository {

    private val runningDataManager = RunningDataManager.getInstance()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
    private var locationCallback: LocationCallback? = null

    override val userLocationState: MutableStateFlow<UserLocation> =
        MutableStateFlow(UserLocation.NotTracking)

    private var isTrackingUserLocation = true

    // TODO API 연결 및 성공 후 WorkerStart, DataStore 를 통해 중간 실패 여부 저장
    override suspend fun startRunning(uid: Long): Result<Long> {
        return runningControlDataSource.runningStart(uid)
    }

    override suspend fun pauseRunning() {
        TODO("Not yet implemented")
    }

    override suspend fun resumeRunning() {
        TODO("Not yet implemented")
    }

    // TODO API 연결 및 성공 후 저장
    override suspend fun finishRunning(
        uid: Long,
        authId: String,
        historyId: Int,
        endTime: String,
        totalTime: Int,
        distance: Double,
        calorie: Int,
        step: Int
        ): Result<List<CompletedRunning>> {
        return runningControlDataSource.runningFinish(uid,
            authId,
            historyId,
            endTime,
            totalTime,
            distance,
            calorie,
            step)
    }

    override fun listenLocation() {
        isTrackingUserLocation = true
        if (userLocationState.value is UserLocation.Tracking) return
        if ((runningDataManager.runningState.value is RunningState.NotRunning).not()) return
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    if (isTrackingUserLocation) {
                        userLocationState.value =
                            UserLocation.Tracking(location.latitude, location.longitude)
                    }
                } ?: run {
                    removeListener()
                }
            }
        }
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                requireNotNull(locationCallback),
                Looper.getMainLooper(),
            )
        } catch (e: SecurityException) {
            removeListener()
        } catch (e: Exception) {
            removeListener()
        }
    }

    override fun removeListener() {
        isTrackingUserLocation = false
        userLocationState.value = UserLocation.NotTracking
        fusedLocationClient.removeLocationUpdates(requireNotNull(locationCallback))
        locationCallback = null
    }

    override fun removeUserLocation() {
        userLocationState.value = UserLocation.NotTracking
    }

    override suspend fun sendLike(uid: Long, receiverId: Long): Result<Long> {
        return runningControlDataSource.sendLike(uid, receiverId)
    }
}
