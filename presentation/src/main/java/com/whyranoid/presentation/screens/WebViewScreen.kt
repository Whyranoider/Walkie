package com.whyranoid.presentation.screens

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String?,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    setBackgroundColor(Color.TRANSPARENT)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        domStorageEnabled = true
                        setSupportZoom(false)
                    }
                }
            },
            update = { webView ->
                webView.loadUrl(url ?: "")
            }
        )
    }
}