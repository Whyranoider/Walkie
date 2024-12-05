package com.whyranoid.presentation.component.challenge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.whyranoid.domain.model.challenge.Challenge
import com.whyranoid.domain.model.challenge.ChallengeType
import com.whyranoid.domain.util.EMPTY
import com.whyranoid.domain.util.getDurationDifference
import com.whyranoid.domain.util.getToday
import com.whyranoid.presentation.theme.ChallengeColor.getColor
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChallengeGoalIndicator(challenge: Challenge) {

    val challengeColor = challenge.challengeType.getColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .background(challengeColor.backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val progressBarColor = challengeColor.progressBarColor

        val progress =
            if (challenge.progress == null) 0f else requireNotNull(challenge.progress) / 100f

        val textMeasure = rememberTextMeasurer()

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ChallengeGoalItem(
                modifier = Modifier.weight(1f),
                goal = "목표",
                limit = when (challenge.challengeType) {
                    ChallengeType.LIFE -> {
                        "연속 " + challenge.period.toString() + "일"
                    }

                    ChallengeType.CALORIE -> {
                        challenge.calorie.toString() + "kcal"
                    }

                    ChallengeType.DISTANCE -> {
                        "${challenge.distance}km"
                    }
                },
            )

            Divider(
                color = challengeColor.progressBarColor,
                modifier = Modifier
                    .height(55.dp)
                    .width(1.dp)
            )

            when (challenge.challengeType) {
                ChallengeType.LIFE -> {
                    if (challenge.startTime != String.EMPTY) {
                        ChallengeGoalItem(
                            modifier = Modifier.weight(1f),
                            goal = "운동 시작 시간",
                            limit = "${challenge.startTime?.take(2)}-${challenge.endTime?.take(2)}시"
                        )
                    } else {
                        ChallengeGoalItem(
                            modifier = Modifier.weight(1f),
                            goal = "일일 운동 시간",
                            limit = "${challenge.time}시간"
                        )
                    }

                }

                ChallengeType.CALORIE, ChallengeType.DISTANCE -> {
                    ChallengeGoalItem(
                        modifier = Modifier.weight(1f),
                        goal = "기간",
                        limit = challenge.timeLimit.toString() + "일 동안"
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        if (challenge.status == "P") {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                when (challenge.challengeType) {
                    ChallengeType.LIFE -> {
                        Text(
                            "${challenge.accCount}일째 성공했어요!",
                            style = WalkieTypography.Body1_ExtraBold.copy(
                                color = challengeColor.progressBarColor
                            )
                        )
                    }

                    ChallengeType.CALORIE -> {
                        Text(
                            "${challenge.calorie?.minus(challenge.accCalories?.toInt() ?: 0)}kcal 만큼 남았어요",
                            style = WalkieTypography.Body1_ExtraBold.copy(
                                color = challengeColor.progressBarColor
                            )
                        )
                    }

                    ChallengeType.DISTANCE -> {
                        Text(
                            "${challenge.distance?.minus(challenge.accDistance?.toInt() ?: 0)}m 만큼 남았어요",
                            style = WalkieTypography.Body1_ExtraBold.copy(
                                color = challengeColor.progressBarColor
                            )
                        )
                    }
                }


                Text(
                    "${challenge.progress}%",
                    style = WalkieTypography.Body1.copy(
                        color = challengeColor.progressBarColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Canvas(
                modifier = Modifier
                    .progressSemantics(progress)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(4.dp)
            ) {

                val width = size.width
                val height = size.height

                val yOffset = height / 2

                val isLtr = layoutDirection == LayoutDirection.Ltr
                val barStart = (if (isLtr) 0f else 1f - progress) * width
                val barEnd = (if (isLtr) progress else 1f) * width

                // draw Background
                drawLine(
                    Color.White,
                    Offset(0f, yOffset),
                    Offset(1f * width, yOffset),
                    2.dp.toPx(),
                    cap = StrokeCap.Round,
                )

                // Progress line
                drawLine(
                    progressBarColor,
                    Offset(barStart, yOffset),
                    Offset(barEnd, yOffset),
                    4.dp.toPx(),
                    cap = StrokeCap.Round,
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            val deadLineInfo = getDurationDifference(
                getToday(), challenge.challengeEdate ?: getToday()
            )

            Text(
                text = "${deadLineInfo[0]}일 ${deadLineInfo[1]}시간 ${deadLineInfo[2]}분" + "남음",
                style = WalkieTypography.Body2.copy(
                    color = Color(0xFF666666)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}
