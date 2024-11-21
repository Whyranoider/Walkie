package com.whyranoid.presentation.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme
import com.whyranoid.presentation.theme.WalkieTypography

@Composable
fun SignInAgreeScreen(
    modifier: Modifier = Modifier,
    onSuccess: (agreeGps: Boolean, agreeMarketing: Boolean) -> Unit,
) {
    val checkedList = remember { List(4) { mutableStateOf(false) } }
    val isAllChecked = checkedList.all { it.value }
    val isEssentialChecked = checkedList[0].value && checkedList[1].value

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(68.dp))
        Text(
            "고객님의 소중한 정보를 보호하기 위해 약관 동의가 필요해요.",
            style = WalkieTypography.Title,
        )
        Spacer(modifier = Modifier.height(32.dp))

        val textColor =
            if (isAllChecked) WalkieColor.Primary else Color.Black

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = textColor,
                    shape = RoundedCornerShape(16.dp),
                )
                .clickable {
                    for (i in checkedList.indices) {
                        checkedList[i].value = true
                    }
                }
                .padding(16.dp),
        ) {
            Text("전체동의(서비스 항목 포함)", style = WalkieTypography.SubTitle, color = textColor)
        }

        Spacer(modifier = Modifier.height(12.dp))

        val textList = listOf(
            "서비스 이용약관(필수)",
            "개인 정보 처리 방침(필수)",
            "위치기반 서비스 이용 동의(선택)",
            "마케팅 정보 수신 및 활용 동의(선택)",
        )
        for (i in checkedList.indices) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checkedList[i].value, onCheckedChange = {
                    checkedList[i].component2().invoke(it)
                }, colors = colors(checkedColor = WalkieColor.Primary))
                Text(textList[i])
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            onClick = {
                onSuccess(checkedList[2].value, checkedList[3].value)
            },
            contentPadding = PaddingValues(vertical = 6.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = isEssentialChecked,
            colors = buttonColors(backgroundColor = WalkieColor.Primary),
            elevation = null,
        ) {
            Text("다음", color = Color.White)
        }
    }
}

@Preview
@Composable
fun PreviewSignInAgreeScreen() {
    WalkieTheme() {}
}
