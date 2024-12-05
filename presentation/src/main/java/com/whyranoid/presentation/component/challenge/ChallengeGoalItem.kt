package com.whyranoid.presentation.component.challenge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            fontSize = 15.sp,
            fontWeight = FontWeight(500)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = limit,
            fontSize = 20.sp,
            fontWeight = FontWeight(700)
        )
    }

}