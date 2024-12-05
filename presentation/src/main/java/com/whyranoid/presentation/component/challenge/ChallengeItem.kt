package com.whyranoid.presentation.component.challenge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.theme.ChallengeColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.bouncingClickable

@Composable
fun ChallengeItem(
    modifier: Modifier = Modifier,
    challengeColor: ChallengeColor.ChallengeColorInterface,
    text: String,
    onClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .bouncingClickable {
                onClicked()
            }
            .fillMaxWidth()
            .height(90.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = challengeColor.backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = text,
                overflow = TextOverflow.Ellipsis,
                style = WalkieTypography.Body1,
                maxLines = 2
            )
        }
    }
}