package com.whyranoid.walkie.walkiedialog

import android.os.Build
import androidx.annotation.RequiresApi

sealed class DialogContentProvider(
    val title: String,
    val description: String,
) {
    object LocationPermission :
        DialogContentProvider(
            "위치 정보 제공 동의",
            "러닝 기능을 사용하려면 사용자의 위치 권한 동의가 반드시 필요해요.",
        ) {
        const val PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
    }

    object StoragePermission : DialogContentProvider(
        "미디어 및 파일 접근 동의",
        "러닝 정보를 기록하려면 미디어 및 파일 접근 권한 동의가 반드시 필요해요.",
    ) {
        const val PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    object GPS : DialogContentProvider(
        "위치 상태 확인 요망",
        "러닝 기능을 사용하려면 위치 정보 상태가 켜져야해요.",
    )

    object Network : DialogContentProvider(
        "네트워크 상태 확인 요망",
        "러닝 기능을 사용하려면 네트워트가 연결되어야해요.",
    )

    object ReadImagePermission : DialogContentProvider(
        "미디어 접근 동의",
        "러닝 정보를 기록하려면 미디어 접근 권한 동의가 반드시 필요해요.",
    ) {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val PERMISSION = android.Manifest.permission.READ_MEDIA_IMAGES
    }
}
