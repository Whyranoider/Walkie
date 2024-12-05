package com.whyranoid.presentation.component.challenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.theme.WalkieTypography

@Composable
fun ChallengeGoalItem(
    modifier: Modifier = Modifier,
    goal: String,
    limit: String
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = goal,
            style = WalkieTypography.Body1,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = limit,
            style = WalkieTypography.Title,
        )
    }

}