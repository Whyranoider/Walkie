package com.whyranoid.presentation.component.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.whyranoid.domain.model.challenge.Challenge
import com.whyranoid.presentation.component.button.WalkieNegativeButton
import com.whyranoid.presentation.component.button.WalkiePositiveButton
import com.whyranoid.presentation.theme.SystemColor
import com.whyranoid.presentation.theme.WalkieTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChallengeExitModalBottomSheetContainer(
    challenge: Challenge,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    modalSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    ),
    onPositiveButtonClicked: (Challenge) -> Unit = {},
    onNegativeButtonClicked: (Challenge) -> Unit = {},
    content: @Composable () -> Unit = {

    }
) {
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "정말 도전을 멈추시겠어요?",
                    style = WalkieTypography.Title
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${challenge.badge.name} 뱃지가 코 앞이에요.",
                    style = WalkieTypography.Body2.copy(color = SystemColor.Negative)
                )

                Spacer(modifier = Modifier.height(31.dp))

                AsyncImage(
                    model = challenge.badge.imageUrl, contentDescription = "",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 10.dp),
                )

                Text(
                    text = "${challenge.badge.name} 뱃지",
                    style = WalkieTypography.Body2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "진행률 ${challenge.progress}%",
                    style = WalkieTypography.Body2.copy(
                        color = SystemColor.Negative,
                        fontWeight = FontWeight(700)
                    )
                )

                Spacer(modifier = Modifier.height(50.dp))

                WalkiePositiveButton(text = "도전하기") {
                    coroutineScope.launch {
                        modalSheetState.hide()
                        onPositiveButtonClicked(challenge)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                WalkieNegativeButton(text = "그만하기") {
                    coroutineScope.launch {
                        modalSheetState.hide()
                        onNegativeButtonClicked(challenge)
                    }
                }
                Spacer(modifier = Modifier.height(19.dp))
            }
        }
    ) {
        content()
    }
}