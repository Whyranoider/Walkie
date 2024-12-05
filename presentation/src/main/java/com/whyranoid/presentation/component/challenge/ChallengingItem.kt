package com.whyranoid.presentation.component.challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.theme.ChallengeColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.bouncingClickable

// TODO: set Color System
@Composable
fun ChallengingItem(
    text: String,
    progress: Float,
    challengeColor: ChallengeColor.ChallengeColorInterface,
    onClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .bouncingClickable {
                onClicked()
            }
            .fillMaxWidth()
            .height(105.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = challengeColor.backgroundColor),
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 26.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                style = WalkieTypography.Body1
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .height(7.dp),
            contentAlignment = Alignment.BottomStart
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .background(color = challengeColor.progressBackgroundColor),
                contentAlignment = Alignment.BottomCenter
            ) {

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(7.dp)
                    .background(color = challengeColor.progressBarColor),
                contentAlignment = Alignment.BottomCenter
            ) {

            }
        }

    }
}
