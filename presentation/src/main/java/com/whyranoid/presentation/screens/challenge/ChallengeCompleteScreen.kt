package com.whyranoid.presentation.screens.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.whyranoid.domain.util.EMPTY
import com.whyranoid.presentation.component.button.WalkieNegativeButton
import com.whyranoid.presentation.component.button.WalkiePositiveButton
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.challenge.ChallengeCompleteViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ChallengeCompleteScreen(
    navController: NavController,
    challengeId: Long,
) {

    val viewModel = koinViewModel<ChallengeCompleteViewModel>()
    val state by viewModel.collectAsState()

    LaunchedEffect(challengeId) {
        viewModel.getChallengeDetail(challengeId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 126.dp),
                text = "챌린지 성공!",
                style = WalkieTypography.Title
            )

            Spacer(modifier = Modifier.height(56.dp))

            AsyncImage(
                model = state.challenge.getDataOrNull()?.badge?.imageUrl
                    ?: "https://picsum.photos/250/250 ", contentDescription = "",
                modifier = Modifier
                //    .size(180.dp)
                ,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.challenge.getDataOrNull()?.name ?: String.EMPTY,
                style = WalkieTypography.Title,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "뱃지 획득!",
                style = WalkieTypography.Title,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "마이페이지에서 확인해보세요",
                style = WalkieTypography.Body1.copy(
                    color = Color(0xFF989898),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = "사진으로 저장하시겠어요?")

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
            ) {
                Box(modifier = Modifier.weight(.5f)) {
                    WalkieNegativeButton(text = "아니오") {
                        navController.popBackStack()
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))

                Box(modifier = Modifier.weight(.5f)) {
                    WalkiePositiveButton(text = "네 좋아요") {

                    }
                }
            }
        }


    }
}