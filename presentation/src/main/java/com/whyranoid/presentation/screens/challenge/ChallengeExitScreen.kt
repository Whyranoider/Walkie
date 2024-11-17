package com.whyranoid.presentation.screens.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.whyranoid.presentation.component.button.WalkieNegativeButton
import com.whyranoid.presentation.component.button.WalkiePositiveButton
import com.whyranoid.presentation.reusable.SingleToast
import com.whyranoid.presentation.reusable.WalkieCircularProgressIndicator
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.challenge.ChallengeExitSideEffect
import com.whyranoid.presentation.viewmodel.challenge.ChallengeExitState
import com.whyranoid.presentation.viewmodel.challenge.ChallengeExitViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChallengeExitScreen(
    navController: NavController,
    challengeId: Long,
) {

    val context = LocalContext.current
    val viewModel = koinViewModel<ChallengeExitViewModel>()

    LaunchedEffect(true) {
        viewModel.getChallengeDetail(challengeId)
    }

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            ChallengeExitSideEffect.StopChallengeSuccess -> {
                SingleToast.show(context, "챌린지를 성공적으로 중단하였습니다.")
                navController.popBackStack()
            }

            ChallengeExitSideEffect.StopChallengeFailure -> {
                SingleToast.show(context, "챌린지 중단에 실패하였습니다.")
            }
        }
    }

    ChallengeExitContent(
        state,
        onPositiveButtonClicked = {
            viewModel.stopChallenge()
        },
        onNegativeButtonClicked = {
            navController.popBackStack()
        })
}

@Composable
fun ChallengeExitContent(
    state: ChallengeExitState,
    onPositiveButtonClicked: () -> Unit = {},
    onNegativeButtonClicked: () -> Unit = {}
) {

    state.challenge.getDataOrNull()?.let { challenge ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "챌린지를 중단할까요?",
                    style = WalkieTypography.Title
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "확인 버튼을 누르면 챌린지가 중단 됩니다.",
                    style = WalkieTypography.Body2.copy(Color(0xFF989898))
                )

                Spacer(modifier = Modifier.height(57.dp))

                AsyncImage(
                    model = challenge.badge.imageUrl, contentDescription = "",
                    modifier = Modifier
                        .size(208.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(17.dp))
                Text(
                    text = challenge.name,
                    style = WalkieTypography.Title.copy(Color(0xFF929292))
                )

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "진행률 ${challenge.progress}%",
                    style = WalkieTypography.Title.copy(
                        color = Color(0xFF929292),
                        fontSize = 15.sp
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    Modifier.weight(1f)
                ) {
                    WalkieNegativeButton(text = "취소") {
                        onNegativeButtonClicked()
                    }
                }

                Box(
                    Modifier.weight(1f)
                ) {
                    WalkiePositiveButton(text = "확인") {
                        onPositiveButtonClicked()
                    }
                }

            }

        }
    } ?: run {
        WalkieCircularProgressIndicator(Modifier.fillMaxSize())
    }


}