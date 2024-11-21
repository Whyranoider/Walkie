package com.whyranoid.presentation.component.running

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyranoid.domain.model.user.User
import com.whyranoid.presentation.component.community.RunningFollowerItem
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme

// TODO 오프라인 때 = 테두리 회색 + 하트 버튼 X, 온라인일 때 = 테두리 주황 + 하트 버튼 O
@Composable
fun RunningFollowerItemWithLikable(
    user: User = User.DUMMY,
    onClickProfile: (user: User) -> Unit = { Log.d("t", "ju0828 called none") },
    onClick: (uid: Long) -> Unit = { Log.d("t", "ju0828 called none") },
    circleBorderColor: Color = WalkieColor.Primary,
    isDisplayName: Boolean = false,
    isLiked: Boolean = false,
) {
    var isLikedStatus by remember { mutableStateOf(isLiked) }

    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.TopEnd,
    ) {
        RunningFollowerItem(
            user = user,
            isDisplayName = isDisplayName,
            circleBorderColor,
            onClickProfile = onClickProfile,
        )
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .clip(CircleShape)
                .clickable {
                    onClick.invoke(user.uid)
                    isLikedStatus = isLikedStatus.not()
                }
                .size(48.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .size(40.dp)
                .padding(4.dp)
                .shadow(elevation = 1.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Color.White),
        ) {
            Icon(
                Icons.Default.Favorite,
                tint = if (isLikedStatus) WalkieColor.Primary else WalkieColor.GrayBorder,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center),
                contentDescription = "like image",
            )
        }
    }
}

@Composable
@Preview
fun RunningFollowerItemWithLikablePreview() {
    WalkieTheme {
        RunningFollowerItemWithLikable(onClick = { Result.success(Unit) })
    }
}
