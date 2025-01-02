package com.whyranoid.presentation.screens.running

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationSource
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.GroundOverlay
import com.naver.maps.map.compose.LocationOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapEffect
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.whyranoid.domain.model.running.UserLocation
import com.whyranoid.domain.model.user.User
import com.whyranoid.presentation.component.running.RunningFollowerItemWithLikable
import com.whyranoid.presentation.model.running.SavingState
import com.whyranoid.presentation.model.running.TrackingMode
import com.whyranoid.presentation.reusable.CircleProgressWithText
import com.whyranoid.presentation.reusable.GalleryGrid
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.dpToPx
import com.whyranoid.presentation.util.toPace
import com.whyranoid.presentation.util.toRunningTime
import com.whyranoid.presentation.viewmodel.RunningScreenState
import com.whyranoid.presentation.viewmodel.RunningViewModel
import com.whyranoid.presentation.viewmodel.RunningViewModel.Companion.MAP_MAX_ZOOM
import com.whyranoid.presentation.viewmodel.RunningViewModel.Companion.MAP_MIN_ZOOM
import com.whyranoid.runningdata.model.RunningFinishData
import com.whyranoid.runningdata.model.RunningState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RunningScreen(
    navController: NavController,
    startWorker: () -> Unit,
) {
    val viewModel = koinViewModel<RunningViewModel>()

    LaunchedEffect(LocalLifecycleOwner.current) {
        viewModel.startWorker = startWorker
        viewModel.getRunningState()
        viewModel.getRunningFollowingsState()
        viewModel.onTrackingButtonClicked()
    }

    val state by viewModel.collectAsState()

    RunningContent(
        state,
        viewModel::startRunning,
        viewModel::onTrackingButtonClicked,
        viewModel::onTrackingCanceledByGesture,
        viewModel::pauseRunning,
        viewModel::resumeRunning,
        viewModel::finishRunning,
        viewModel::openEdit,
        viewModel::closeEdit,
        viewModel::selectImage,
        viewModel::saveHistory,
        viewModel::takeSnapShot,
        viewModel::sendLike,
        viewModel::removeImage,
        onClickProfile = { user ->
            navController.navigate("userPage/${user.uid}/${user.nickname}/${true}")
        },
    )
}

@Composable
fun RunningContent(
    state: RunningScreenState,
    onStartRunning: () -> Unit,
    onClickTrackingModeButton: () -> Unit,
    onTrackingCanceledByGesture: () -> Unit,
    onPauseRunning: () -> Unit,
    onResumeRunning: () -> Unit,
    onFinishRunning: () -> Unit,
    onEditOpen: () -> Unit,
    onEditClose: () -> Unit,
    selectImage: (Uri) -> Unit,
    saveHistory: (Bitmap, RunningFinishData) -> Unit,
    onTakeSnapShot: (RunningFinishData) -> Unit,
    onSendLike: (uid: Long) -> Unit,
    onRemoveImage: () -> Unit,
    onClickProfile: (user: User) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
    ) {
        RunningMapScreen(
            modifier = Modifier.weight(1f, false),
            state,
            onClickTrackingModeButton,
            onTrackingCanceledByGesture,
            onEditOpen,
            onEditClose,
            saveHistory,
            onSendLike,
            onClickProfile,
            onRemoveImage,
        )
        RunningInfoScreen(
            modifier = Modifier.height(280.dp),
            state = state,
            onSelectImage = selectImage,
            onPermissionDismiss = onEditClose,
        )
    }
    RunningBottomButton(
        modifier = Modifier
            .height(50.dp)
            .width(80.dp),
        state = state,
        onStartRunning = onStartRunning,
        onPauseRunning = onPauseRunning,
        onResumeRunning = onResumeRunning,
        onFinishRunning = onFinishRunning,
        onEditClose = onEditClose,
        onTakeSnapShot = onTakeSnapShot,
    )

    state.savingState.getDataOrNull()?.let {
        if (it is SavingState.Start) CircleProgressWithText(text = "저장 중")
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun RunningMapScreen(
    modifier: Modifier = Modifier,
    state: RunningScreenState,
    onClickTrackingModeButton: () -> Unit,
    onTrackingCanceledByGesture: () -> Unit,
    onEditOpen: () -> Unit,
    onEditClose: () -> Unit,
    onSaveHistory: (Bitmap, RunningFinishData) -> Unit,
    onSendLike: (uid: Long) -> Unit,
    onClickProfile: (user: User) -> Unit,
    onRemoveImage: () -> Unit,
) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoom = MAP_MAX_ZOOM,
                minZoom = MAP_MIN_ZOOM,
                locationTrackingMode = LocationTrackingMode.Follow,
            ),
        )
    }

    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = false,
                isScaleBarEnabled = false,
                isZoomControlEnabled = false,
                isCompassEnabled = false,
                isLogoClickEnabled = false,
                logoMargin = PaddingValues(bottom = 44.dp, start = 18.dp),
            ),
        )
    }

    mapUiSettings = MapUiSettings(
        isScrollGesturesEnabled = state.runningFinishState.getDataOrNull() == null,
        isZoomGesturesEnabled = state.runningFinishState.getDataOrNull() == null,
        isLocationButtonEnabled = false,
        isScaleBarEnabled = false,
        isZoomControlEnabled = false,
        isCompassEnabled = false,
        isLogoClickEnabled = false,
        logoMargin = PaddingValues(bottom = 44.dp, start = 18.dp),
    )

    mapProperties = MapProperties(
        mapType = if (state.selectedImage.getDataOrNull() != null) MapType.None else MapType.Basic,
        maxZoom = MAP_MAX_ZOOM,
        minZoom = MAP_MIN_ZOOM,
        locationTrackingMode = LocationTrackingMode.Follow,
    )

    val cameraPositionState = rememberCameraPositionState().apply {
        state.runningFinishState.getDataOrNull()?.let { runningFinishData ->
            var maxLat = Double.MIN_VALUE
            var minLat = Double.MAX_VALUE
            var maxLng = Double.MIN_VALUE
            var minLng = Double.MAX_VALUE
            runningFinishData.runningPositionList.flatten().map { position ->
                maxLat = maxOf(maxLat, position.latitude)
                minLat = minOf(minLat, position.latitude)
                maxLng = maxOf(maxLng, position.longitude)
                minLng = minOf(minLng, position.longitude)
            }
            val cameraUpdate = CameraUpdate.fitBounds(
                LatLngBounds(
                    LatLng(maxLat, maxLng),
                    LatLng(minLat, minLng),
                ),
                100.dpToPx(LocalContext.current),
            )
            move(cameraUpdate)
        } ?: run {
            // 러닝중일 때 카메라 위치
            state.trackingModeState.getDataOrNull()?.let { trackingMode ->
                state.runningState.getDataOrNull()?.runningData?.lastLocation?.let { location ->
                    if (trackingMode == TrackingMode.FOLLOW) {
                        move(CameraUpdate.scrollTo(LatLng(location)))
                    }
                }
            }
        }
    }

    Box(modifier) {
        NaverMap(
            cameraPositionState = cameraPositionState,
            modifier = modifier,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            locationSource = rememberCustomLocationSource(),
        ) {
            if (cameraPositionState.cameraUpdateReason.value == CameraUpdate.REASON_GESTURE) onTrackingCanceledByGesture()

            MapEffect(state.savingState) { map ->
                state.savingState.getDataOrNull()?.let { savingState ->
                    if (savingState is SavingState.Start) {
                        map.takeSnapshot { bitmap ->
                            onSaveHistory(bitmap, savingState.runningFinishData)
                        }
                    }
                }
            }

            // 러닝 시작전 사용자 위치
            state.userLocationState.getDataOrNull()?.let {
                if (it is UserLocation.Tracking) {
                    val location = Location("").apply {
                        latitude = it.lat
                        longitude = it.lng
                    }
                    LaunchedEffect(Unit) {
                        cameraPositionState.move(
                            CameraUpdate.scrollTo(LatLng(location)),
                        )
                    }
                    LocationOverlay(
                        position = LatLng(location),
                        // icon = OverlayImage.fromResource(R.drawable.ic_running_screen_selected),
                        circleOutlineWidth = 2.dp,
                        circleOutlineColor = Color.White,
                    )
                }
            }

            // 러닝 중 사용자 경로 표시
            state.runningState.getDataOrNull()?.let {
                it.runningData.lastLocation?.let { location ->
                    LocationOverlay(
                        position = LatLng(location),
                        // icon = OverlayImage.fromResource(R.drawable.ic_running_screen_selected),
                        circleOutlineWidth = 2.dp,
                        circleOutlineColor = Color.White,
                    )
                }

                DrawPath(it.runningData.runningPositionList)
            }

            // 러닝 종료 후 그림
            state.runningFinishState.getDataOrNull()?.let { finData ->
                // 경로
                DrawPath(finData.runningPositionList)
            }

            state.selectedImage.getDataOrNull()?.let { uri ->
                cameraPositionState.contentBounds?.let { ll ->
                    val bitmap = if (Build.VERSION.SDK_INT >= 28) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                LocalContext.current.contentResolver,
                                uri,
                            ),
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            LocalContext.current.contentResolver,
                            uri,
                        )
                    }
                    DrawBitmap(bitmap = bitmap, ll = ll)
                }
            }
        } // End Of NaverMap

        // Out of NaverMap
        // 편집 중
        state.editState.getDataOrNull()?.let { editState ->
            if (editState) {
                // 탑 앱바
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Icon(
                        Icons.Default.Close,
                        "close",
                        modifier = Modifier.clickable { onEditClose() },
                    )
                    Text("이미지", style = WalkieTypography.SubTitle)

                    Text("초기화", style = WalkieTypography.Body1_Normal, modifier = Modifier.clickable {
                        onRemoveImage()
                    })


//                    Icon(
//                        Icons.Outlined.PhotoCamera,
//                        "camera",
//                        modifier = Modifier.clickable {
//                            /* TODO */
//                        },
//                    )
                }
            }
        }

        // 러닝 종료 시 오버레이 Overlay  제외
        state.runningFinishState.getDataOrNull()?.let { finData ->
            if (state.editState.getDataOrNull() != true) {
                Text(
                    text = "편집",
                    style = WalkieTypography.SubTitle,
                    modifier = Modifier
                        .clickable { onEditOpen() }
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                )
            }

            // 지도 하단 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                listOf(
                    "%.2f".format(finData.runningHistory.totalDistance.div(1000.toDouble())),
                    finData.runningHistory.totalRunningTime.toRunningTime(),
                    finData.runningHistory.pace.toPace(),
                ).forEach {
                    Text(
                        it,
                        style = WalkieTypography.Title,
                    )
                }
            }
        } ?: run {
            // 러닝 중 지도 위 아이콘 및 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { /* TODO */ }
                        .background(Color.White)
                        .height(24.dp)
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(4.dp),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                        tint = WalkieColor.Primary,
                    )
                    Text(
                        text = "0",
                        style = WalkieTypography.Body1,
                        modifier = Modifier.padding(end = 4.dp),
                    )
                }

                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            state.runningState.getDataOrNull()?.runningData?.lastLocation?.let { location ->
                                if (state.trackingModeState.getDataOrNull() == TrackingMode.NONE) {
                                    cameraPositionState.move(
                                        CameraUpdate.scrollTo(LatLng(location)),
                                    )
                                }
                            } ?: state.userLocationState
                                .getDataOrNull()
                                ?.let { location ->
                                    if (location is UserLocation.Tracking) {
                                        cameraPositionState.move(
                                            CameraUpdate.scrollTo(
                                                LatLng(
                                                    location.lat,
                                                    location.lng,
                                                ),
                                            ),
                                        )
                                    }
                                }
                            onClickTrackingModeButton()
                        }
                        .background(Color.White)
                        .size(24.dp)
                        .padding(4.dp),
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "",
                    tint = WalkieColor.Primary,
                )
            }

            LazyRow {
                state.runningFollowerState.getDataOrNull()?.let { (running, notRunning) ->
                    items(running.size) {
                        RunningFollowerItemWithLikable(
                            user = running[it].user,
                            onClickProfile = onClickProfile,
                            onClick = onSendLike,
                            circleBorderColor = WalkieColor.Primary,
                            isLiked = running[it].isLiked,
                        )
                    }
                    items(notRunning.size) {
                        RunningFollowerItemWithLikable(
                            user = notRunning[it],
                            circleBorderColor = WalkieColor.GrayBorder,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RunningInfoScreen(
    state: RunningScreenState,
    modifier: Modifier = Modifier,
    onSelectImage: (Uri) -> Unit,
    onPermissionDismiss: () -> Unit,
) {
    if (state.editState.getDataOrNull() == true) {
        Column {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "앨범", style = WalkieTypography.SubTitle)
            }
            GalleryGrid(
                column = 3,
                modifier = modifier
                    .background(Color.White),
                onImageSelected = { uri -> onSelectImage(uri) },
                onPermissionDismiss = onPermissionDismiss,
            )
        }
        return
    }
    Column(
        modifier = modifier
            .padding(bottom = 66.dp)
            .background(Color.White),
    ) {
        // 거리, 런닝 시간
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "거리",
                    style = WalkieTypography.Body2,
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "%.2f".format(
                            state.runningResultInfoState.getDataOrNull()?.distance?.div(1000.toDouble())
                                ?: state.runningInfoState.getDataOrNull()?.distance?.div(1000.toDouble())
                                ?: 0.00,
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = "km",
                        style = WalkieTypography.Body2.copy(fontSize = 16.sp),
                    )
                }
            }

            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "런닝시간",
                    style = WalkieTypography.Body2,
                )
                Text(
                    text = state.runningResultInfoState.getDataOrNull()?.runningTime?.toRunningTime()
                        ?: state.runningInfoState.getDataOrNull()?.runningTime?.toRunningTime()
                        ?: "00:00:00",
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                )
            }
        }

        // 페이스 칼로리 걸음수
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "페이스",
                    style = WalkieTypography.Body2,
                )
                Text(
                    text = state.runningResultInfoState.getDataOrNull()?.pace?.toPace()
                        ?: state.runningInfoState.getDataOrNull()?.pace?.toPace() ?: "0`00``",
                    style = WalkieTypography.Title,
                )
            }

            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "칼로리",
                    style = WalkieTypography.Body2,
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${state.runningResultInfoState.getDataOrNull()?.calories?.toInt() ?: state.runningInfoState.getDataOrNull()?.calories?.toInt() ?: 0}",
                        style = WalkieTypography.Title,
                    )
                    Text(
                        text = "kcal",
                        style = WalkieTypography.SubTitle,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "걸음수",
                    style = WalkieTypography.Body2,
                )
                Text(
                    text = state.runningResultInfoState.getDataOrNull()?.steps?.toString()
                        ?: state.runningInfoState.getDataOrNull()?.steps?.toString()
                        ?: "0",
                    style = WalkieTypography.Title,
                )
            }
        }


    }
}

@Composable
fun RunningBottomButton(
    modifier: Modifier = Modifier,
    state: RunningScreenState,
    onStartRunning: () -> Unit,
    onPauseRunning: () -> Unit,
    onResumeRunning: () -> Unit,
    onFinishRunning: () -> Unit,
    onEditClose: () -> Unit,
    onTakeSnapShot: (RunningFinishData) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        // 편집 중
        if (state.editState.getDataOrNull() == true) {
            Button(
                modifier = Modifier
                    .height(44.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                onClick = { onEditClose() },
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    "확인",
                    style = WalkieTypography.Title,
                )
            }
        } else {
            state.runningFinishState.getDataOrNull()?.let { finState -> // 종료 시
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(modifier = Modifier.padding(bottom = 8.dp), text = "런닝을 종료했습니다.")
                    Button(
                        modifier = Modifier
                            .height(44.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        onClick = { onTakeSnapShot(finState) },
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            "저장",
                            style = WalkieTypography.Title,
                        )
                    }
                }
            } ?: state.runningState.getDataOrNull()?.let { runningState -> // 러닝 시작 전
                when (runningState) {
                    is RunningState.NotRunning -> {
                        Button(
                            modifier = Modifier
                                .height(44.dp)
                                .width(160.dp),
                            onClick = onStartRunning,
                        ) {
                            Text("러닝 시작", style = WalkieTypography.Title)
                        }
                    }

                    is RunningState.Paused -> {
                        var buttonsWidth by remember { mutableFloatStateOf(0f) }

                        Column {
                            Row(
                                Modifier
                                    .height(32.dp)
                                    .width(with(LocalDensity.current) { buttonsWidth.toDp() })
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(WalkieColor.GrayBlack),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "잠시 런닝을 중단합니다",
                                    style = WalkieTypography.Body2.copy(color = WalkieColor.PrimarySurface)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                Modifier
                                    .height(44.dp)
                                    .wrapContentWidth()
                                    .border(
                                        width = 1.dp,
                                        color = WalkieColor.GrayDefault,
                                        shape = CircleShape,
                                    )
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .onSizeChanged {
                                        buttonsWidth = it.width.toFloat()
                                    },
                            ) {
                                IconButton(modifier = modifier, onClick = { onResumeRunning() }) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "",
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                                IconButton(modifier = modifier, onClick = { onFinishRunning() }) {
                                    Icon(
                                        Icons.Default.Stop,
                                        contentDescription = "",
                                        tint = WalkieColor.Primary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }
                    }

                    is RunningState.Running -> {
                        Row(
                            Modifier
                                .height(44.dp)
                                .wrapContentWidth()
                                .border(
                                    width = 1.dp,
                                    color = WalkieColor.GrayDefault,
                                    shape = CircleShape,
                                )
                                .clip(CircleShape)
                                .background(Color.White),
                        ) {
                            IconButton(modifier = modifier, onClick = { onPauseRunning() }) {
                                Icon(
                                    Icons.Default.Pause,
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                            IconButton(modifier = modifier, onClick = { onFinishRunning() }) {
                                Icon(
                                    Icons.Default.Stop,
                                    contentDescription = "",
                                    tint = WalkieColor.Primary,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberCustomLocationSource(): LocationSource {
    return remember {
        object : LocationSource {

            var listener: LocationSource.OnLocationChangedListener? = null

            override fun activate(listener: LocationSource.OnLocationChangedListener) {
                this.listener = listener
            }

            override fun deactivate() {
                listener = null
            }
        }
    }
}

@Composable
@NaverMapComposable
fun DrawPath(paths: List<List<com.whyranoid.runningdata.model.RunningPosition>>) {
    paths.forEach { path ->
        if (path.size > 2) {
            PathOverlay(
                coords = path.map { position ->
                    LatLng(
                        position.latitude,
                        position.longitude,
                    )
                },
                color = WalkieColor.Primary,
                width = 8.dp,
                outlineWidth = 0.dp,
            )
        }
    }
}

@Composable
fun DrawBitmap(bitmap: Bitmap, ll: LatLngBounds) {
    val imageOverlay = OverlayImage.fromBitmap(bitmap)
    var bounds = ll
    val width = imageOverlay.getIntrinsicWidth(LocalContext.current)
    val height = imageOverlay.getIntrinsicHeight(LocalContext.current)

    if (width != 0 && height != 0) {
        // TODO 비율 맞춰서 사진 확장
        // TODO 백그라운드
        val n = ll.northLatitude
        val s = ll.southLatitude
        val e = ll.eastLongitude
        val w = ll.westLongitude
        val imageRatio = height / width.toDouble()
        val mapRatio = (n - s) / (e - w)
        if (imageRatio > mapRatio) { // 높이가 높은 사진
            val h = n - s
            val center = (n + s) / 2
            val weight = ((h / 2) * (imageRatio / mapRatio))
            bounds = LatLngBounds(
                LatLng(center - weight, (w + e) / 2 - ((e - w) / 2) * 1.2),
                LatLng(center + weight, (w + e) / 2 + ((e - w) / 2) * 1.2),
            )

            Log.d(
                "imageRatio",
                "$imageRatio ,${
                    (bounds.northLatitude - bounds.southLatitude) / (bounds.eastLongitude - bounds.westLongitude)
                }",
            )
        } else {
            val wi = e - w
            val center = (e + w) / 2
            val weight = ((wi / 2) * ((mapRatio / imageRatio)))
            bounds = LatLngBounds(
                LatLng(s, center - weight * 1.2),
                LatLng(n, center + weight * 1.2),
            )
        }
    }

    GroundOverlay(
        bounds = bounds,
        image = imageOverlay,
    )
}
