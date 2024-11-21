package com.whyranoid.presentation.component.community

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.whyranoid.domain.model.user.User
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme

@Composable
fun RunningFollowerItem(
    user: User = User.DUMMY,
    isDisplayName: Boolean = true,
    circularBorderColor: Color = WalkieColor.Primary,
    onClickProfile: (user: User) -> Unit = { },
) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .border(2.dp, circularBorderColor, CircleShape)
                .clip(CircleShape)
                .clickable {
                    onClickProfile(user)
                },
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = "달리고 있는 친구의 프로필 이미지",
                modifier = Modifier
                    .size(65.dp)
                    .border(0.5.dp, WalkieColor.GrayBorder, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }

        if (isDisplayName) {
            Spacer(modifier = Modifier.size(6.dp))

            Text(text = "내 기록") // 내꺼면 내기록 아니면 사람 이름
        }
    }
}

@Preview
@Composable
fun RunningFollowerItemPreview() {
    WalkieTheme {
        RunningFollowerItem()
    }
}
