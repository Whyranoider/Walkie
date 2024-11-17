package com.whyranoid.presentation.screens.challenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.whyranoid.presentation.R
import com.whyranoid.presentation.component.ChallengeGoalContent
import com.whyranoid.presentation.component.UserIcon
import com.whyranoid.presentation.component.bottomsheet.ChallengeExitModalBottomSheetContainer
import com.whyranoid.presentation.component.button.WalkiePositiveButton
import com.whyranoid.presentation.reusable.SingleToast
import com.whyranoid.presentation.reusable.WalkieCircularProgressIndicator
import com.whyranoid.presentation.theme.SystemColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.challenge.ChallengeDetailSideEffect
import com.whyranoid.presentation.viewmodel.challenge.ChallengeDetailState
import com.whyranoid.presentation.viewmodel.challenge.ChallengeDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChallengeDetailScreen(
    navController: NavController,
    challengeId: Long,
    isChallenging: Boolean
) {

    val context = LocalContext.current
    val viewModel = koinViewModel<ChallengeDetailViewModel>()

    LaunchedEffect(true) {
        viewModel.getChallengeDetail(challengeId)
    }

    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            ChallengeDetailSideEffect.StartChallengeSuccess -> {
                SingleToast.show(context, "챌린지를 성공적으로 시작하였습니다.")
                navController.popBackStack()
            }

            ChallengeDetailSideEffect.StartChallengeFailure -> {
                SingleToast.show(context, "챌린지를 시작할 수 없습니다.")
            }
        }
    }

    ChallengeDetailContent(state, isChallenging,
        onNegativeButtonClicked = {
            navController.navigate(
                "challengeExit/${challengeId}"
            )
        },
        onStartChallengeButtonClicked = {
            viewModel.startChallenge(it)
        })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChallengeDetailContent(
    state: ChallengeDetailState,
    isChallenging: Boolean,
    onNegativeButtonClicked: (Long) -> Unit = {},
    onStartChallengeButtonClicked: (Int) -> Unit = { }
) {

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    state.challenge.getDataOrNull()?.let { challenge ->

        ChallengeExitModalBottomSheetContainer(
            challenge = challenge,
            coroutineScope = coroutineScope,
            modalSheetState = modalSheetState,
            onNegativeButtonClicked = {
                onNegativeButtonClicked(challenge.id.toLong())
            }
        ) {
            Scaffold() { paddingValues ->

                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState),
                ) {

                    AsyncImage(
                        model = challenge.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    ) {

                        Text(
                            text = challenge.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight(700),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    Color(0xFFF7F7F7),
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 18.dp,
                                    vertical = 12.dp
                                ),
                                color = Color(0xFF989898),
                                text = challenge.content,
                                fontSize = 15.sp,
                                fontWeight = FontWeight(500),
                            )
                        }



                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "도전 내용",
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        ChallengeGoalContent(challenge)

                        Spacer(modifier = Modifier.height(42.dp))

                        Text(
                            text = "성공 시 달성 뱃지",
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = challenge.badge.imageUrl, contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = challenge.badge.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = "함께 도전하는 워키들",
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${challenge.walkies.count()}명",
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow {
                            challenge.walkies.forEach { participant ->
                                item {
                                    UserIcon(user = participant)
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }

                        if (isChallenging) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(130.dp))
                                Text(
                                    modifier = Modifier.clickable {
                                        coroutineScope.launch {
                                            modalSheetState.show()
                                        }
                                    },
                                    text = "그만할래요",
                                    style = WalkieTypography.Body1.copy(SystemColor.Negative),
                                    textDecoration = TextDecoration.Underline
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        } else {
                            Spacer(modifier = Modifier.height(28.dp))
                            WalkiePositiveButton(text = "도전하기") {
                                onStartChallengeButtonClicked(challenge.id.toInt())
                            }
                        }

                    }
                }
            }
        }
    } ?: run {
        WalkieCircularProgressIndicator(Modifier.fillMaxSize())
    }


}


