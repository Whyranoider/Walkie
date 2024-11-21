package com.whyranoid.presentation.util

import android.content.Context

object AppVersionUtil {
    fun getVersionName(context: Context): String? {
        return runCatching {
            context.packageManager
                .getPackageInfo(context.packageName, 0)
                .versionName
        }.getOrNull()
    }
}