package com.whyranoid.presentation.screens.mypage.addpost

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapEffect
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.whyranoid.domain.model.post.TextVisibleState
import com.whyranoid.domain.model.running.RunningHistory
import com.whyranoid.domain.util.DATE_FORMAT
import com.whyranoid.presentation.R
import com.whyranoid.presentation.model.running.toRunningHistoryUiModel
import com.whyranoid.presentation.reusable.CircleProgressWithText
import com.whyranoid.presentation.reusable.GalleryGrid
import com.whyranoid.presentation.screens.running.DrawBitmap
import com.whyranoid.presentation.screens.running.DrawPath
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.dpToPx
import com.whyranoid.presentation.util.toPace
import com.whyranoid.presentation.util.toRunningTime
import com.whyranoid.presentation.viewmodel.AddPostViewModel
import com.whyranoid.presentation.viewmodel.PostUploadingState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun PostingScreen(runningHistory: RunningHistory, finish: () -> Unit) {
    var textVisibleState by remember { mutableStateOf(TextVisibleState.WHITE) }
    var photoEditState by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val viewModel = koinViewModel<AddPostViewModel>()
    val isUploading = viewModel.uploadingState.collectAsStateWithLifecycle()

    var runningHistoryState by remember { mutableStateOf(runningHistory) }

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 80.dp)
            .wrapContentHeight(),
        state = listState,
        verticalArrangement = Arrangement.Top,
    ) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
            ) {
                Text(
                    style = WalkieTypography.Title,
                    text = "새 게시물",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 24.dp),
                )

                Text(
                    style = WalkieTypography.Body1_Normal,
                    text = "초기화",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(bottom = 24.dp).clickable {
                            runningHistoryState = runningHistory.copy(bitmap = null)
                        },
                )
            }

            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SimpleDateFormat("yyyy/MM/dd HH:mm").format(Date(runningHistory.finishedAt))
                    .split(' ')
                    .forEach {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(WalkieColor.GrayDefault),
                        ) {
                            Text(text = it, style = WalkieTypography.SubTitle)
                        }
                    }
            }


            Map(runningHistoryState, textVisibleState, isUploading.value, text)

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
                    .wrapContentHeight(),
            ) {
                if (photoEditState) {
                    Text("앨범", style = WalkieTypography.SubTitle)
                    Spacer(Modifier.weight(1f))
                }
                Icon(
                    modifier = Modifier
                        .clickable {
                            textVisibleState = when (textVisibleState) {
                                TextVisibleState.WHITE -> TextVisibleState.BLACK
                                TextVisibleState.BLACK -> TextVisibleState.HIDE
                                TextVisibleState.HIDE -> TextVisibleState.WHITE
                            }
                        }
                        .size(48.dp)
                        .clip(CircleShape)
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "textVisible",
                    tint = WalkieColor.GrayDefault,
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            photoEditState = photoEditState.not()
                        }
                        .size(48.dp)
                        .clip(CircleShape)
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.ic_gallery),
                    contentDescription = "gallery",
                    tint = WalkieColor.GrayDefault,
                )
            }

            val focusManager = LocalFocusManager.current

            if (photoEditState) {
                GalleryGrid(
                    column = 3,
                    onImageSelected = { uri ->
                        runningHistoryState = runningHistoryState.copy(bitmap = uri.toString())
                    },
                    onPermissionDismiss = {
                        photoEditState = false
                    },
                )
            } else {
                BasicTextField(
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(92.dp)
                        .background(Color.White)
                        .border(1.dp, WalkieColor.GrayDefault, RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    singleLine = false,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Button(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                if (photoEditState) {
                    photoEditState = false
                } else if (isUploading.value == PostUploadingState.Init) {
                    viewModel.startUploading()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = WalkieColor.Primary),
        ) {
            Text(text = if (photoEditState) "확인" else "올리기", color = Color.White)
        }
    }

    when (isUploading.value) {
        PostUploadingState.Uploading -> CircleProgressWithText(text = "업로드 중")
        PostUploadingState.Done -> {
            // TODO
            finish()
        }

        PostUploadingState.Error -> {
            // TODO
        }

        else -> Unit
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun Map(
    runningHistory: RunningHistory,
    textVisibleState: TextVisibleState,
    isUploading: PostUploadingState,
    content: String,
) {
    val runningHistoryUiModel = runningHistory.toRunningHistoryUiModel(LocalContext.current)

    val cameraPositionState = rememberCameraPositionState()

    val viewModel = koinViewModel<AddPostViewModel>()

    val updatedContent by rememberUpdatedState(content)
    val contentHistory = listOf(
        "%.2f".format(runningHistoryUiModel.distance.div(1000.toDouble())),
        runningHistoryUiModel.totalRunningTime.toRunningTime(),
        runningHistoryUiModel.pace.toPace(),
    )

    var maxLat = Double.MIN_VALUE
    var minLat = Double.MAX_VALUE
    var maxLng = Double.MIN_VALUE
    var minLng = Double.MAX_VALUE
    runningHistoryUiModel.paths.flatten().forEach { position ->
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
    LaunchedEffect(Unit) {
        cameraPositionState.move(cameraUpdate)
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isScrollGesturesEnabled = false,
                isLocationButtonEnabled = false,
                isScaleBarEnabled = false,
                isZoomControlEnabled = false,
                isCompassEnabled = false,
                isLogoClickEnabled = false,
                logoMargin = PaddingValues(bottom = 44.dp, start = 18.dp),
            ),
        )
    }

    val mapProperties = MapProperties(
        mapType = if (runningHistoryUiModel.bitmap != null) MapType.None else MapType.Basic,
        locationTrackingMode = LocationTrackingMode.None,
    )

    Box {
        NaverMap(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .aspectRatio(1f),
            cameraPositionState = cameraPositionState,
            uiSettings = mapUiSettings,
            properties = mapProperties,
            locationSource = null,
        ) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            MapEffect(isUploading) { naverMap ->
                if (isUploading == PostUploadingState.Uploading) {
                    naverMap.takeSnapshot { bitmap ->
                        scope.launch {
                            val storage: File = context.cacheDir
                            val fileName = "${System.currentTimeMillis()}.jpg"
                            val tempFile = File(storage, fileName)
                            try {
                                // 자동으로 빈 파일을 생성합니다.
                                tempFile.createNewFile()

                                // 파일을 쓸 수 있는 스트림을 준비합니다.
                                val out = FileOutputStream(tempFile)

                                // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                                viewModel.uploadPost(
                                    updatedContent,
                                    textVisibleState,
                                    "${
                                        SimpleDateFormat(String.DATE_FORMAT).format(
                                            Date(
                                                runningHistory.finishedAt,
                                            ),
                                        )
                                    }_${
                                        getAddress(
                                            context,
                                            runningHistoryUiModel.paths.last().last().latitude,
                                            runningHistoryUiModel.paths.last().last().longitude,
                                        )
                                    }_${contentHistory.joinToString("_")}",
                                    tempFile.absolutePath.toUri().toString(),
                                )
                                out.close()
                            } catch (e: FileNotFoundException) {
                                Log.e("MyTag", "FileNotFoundException : " + e.message)
                            } catch (e: IOException) {
                                Log.e("MyTag", "IOException : " + e.message)
                            }
                        }
                    }
                }
            }

            // 경로
            DrawPath(
                runningHistoryUiModel.paths.map { list ->
                    list.map {
                        com.whyranoid.runningdata.model.RunningPosition(
                            it.latitude,
                            it.longitude,
                        )
                    }
                },
            )

            // 배경이 있는 경우 TODO 배경이 없는 경우
            runningHistoryUiModel.bitmap?.let { bitmap ->
                cameraPositionState.contentBounds?.let { ll ->
                    DrawBitmap(bitmap = bitmap, ll = ll)
                }
            }
        }

        // 지도 하단 정보
        if (textVisibleState != TextVisibleState.HIDE) {
            val textColor =
                if (textVisibleState == TextVisibleState.WHITE) Color.White else Color.Black

            Text(
                text = SimpleDateFormat("yyyy.MM.dd HH:mm").format(Date(runningHistory.finishedAt)),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.TopCenter),
                style = WalkieTypography.Body2.copy(color = textColor),
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                contentHistory.forEach {
                    Text(
                        it,
                        style = WalkieTypography.Title.copy(color = textColor),
                    )
                }
            }
        }
    }
}

fun getAddress(mContext: Context?, lat: Double, lng: Double): String {
    var nowAddr = "위치를 확인 할 수 없습니다."
    val geocoder = mContext?.let { Geocoder(it, Locale.KOREA) }
    val address: List<Address>?
    try {
        if (geocoder != null) {
            address = geocoder.getFromLocation(lat, lng, 1)
            if (address != null && address.isNotEmpty()) {
                nowAddr = address[0].getAddressLine(0).toString()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return nowAddr
}
