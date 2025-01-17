package com.whyranoid.presentation.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme
import com.whyranoid.presentation.theme.WalkieTypography

@Composable
fun CircleProgressWithText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(0.5f)
            .background(WalkieColor.GrayDefault)
            .pointerInput(Unit) { // 터치 이벤트 소비
                detectTapGestures(onPress = {
                    // 아무것도 하지 않음, 터치 이벤트 소비
                })
            })
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .width(200.dp)
                .height(160.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (text.isNotEmpty()) {
                Text(text, style = WalkieTypography.SubTitle)
                Spacer(modifier = Modifier.height(20.dp))
            }
            CircularProgressIndicator()
        }
    }
}

@Composable
@Preview
fun CircleProgressWithTextPreview() {
    WalkieTheme {
        CircleProgressWithText(text = "로딩 중")
    }
}
