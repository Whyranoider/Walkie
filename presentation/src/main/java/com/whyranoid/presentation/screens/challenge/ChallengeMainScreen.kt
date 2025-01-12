package com.whyranoid.presentation.screens.challenge

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.whyranoid.domain.model.challenge.ChallengePreview
import com.whyranoid.domain.model.challenge.ChallengeType
import com.whyranoid.presentation.R
import com.whyranoid.presentation.component.challenge.ChallengeItem
import com.whyranoid.presentation.component.challenge.ChallengingItem
import com.whyranoid.presentation.component.bar.WalkieTopBar
import com.whyranoid.presentation.reusable.WalkieCircularProgressIndicator
import com.whyranoid.presentation.theme.ChallengeColor.getColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.chunkedList
import com.whyranoid.presentation.viewmodel.challenge.ChallengeMainState
import com.whyranoid.presentation.viewmodel.challenge.ChallengeMainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ChallengeMainScreen(
    navController: NavController
) {

    val viewModel = koinViewModel<ChallengeMainViewModel>()

    val state by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.apply {
            getNewChallengeItems()
            getChallengingItems()
            getTypedChallengeItems()
            getTopRankChallengeItems()
        }
    }

    ChallengeMainContent(
        state,
        onChallengeItemClicked = { challengePreview, isChallenging ->
            val route = "challengeDetail/${challengePreview.id}/$isChallenging"
            navController.navigate(route)
        },
        onExpandButtonClicked = {

        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChallengeMainContent(
    state: ChallengeMainState,
    onChallengeItemClicked: (ChallengePreview, Boolean) -> Unit = { _, _ -> },
    onExpandButtonClicked: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            WalkieTopBar(
                leftContent = {
                    Text(
                        text = "챌린지",
                        style = WalkieTypography.Title,
                    )
                },
                rightContent = {
                    Row {
                        Icon(
                            modifier = Modifier
                                .clickable {

                                },
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "메뉴 버튼"
                        )
                    }

                },
            )
        },
    ) { paddingValues ->
        val pagerState = rememberPagerState() {
            ChallengeType.values().size
        }


        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "도전중인 챌린지",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(16.dp))

            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    state.challengingPreviewsState.getDataOrNull()?.let { challengingPreviews ->

                        if (challengingPreviews.isNotEmpty()) {
                            challengingPreviews.take(3).forEach {
                                ChallengingItem(
                                    text = it.title,
                                    progress = it.progress!!,
                                    challengeColor = it.type.getColor(),
                                ) {
                                    onChallengeItemClicked(it, true)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .clip(shape = RoundedCornerShape(7.dp))
                                    .background(Color(0xFFF7F7F7))
                                    .clickable {

                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "더보기",
                                    style = WalkieTypography.Body2,
                                    color = Color(0xFF808080),
                                    fontWeight = FontWeight(700)
                                )
                            }

                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(105.dp)
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .background(Color(0xFFF7F7F7)),
                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "아직 시작한 챌린지가 없어요.",
                                        style = WalkieTypography.Body1,
                                        color = Color(0xFF808080)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "아래 목록에서 도전할 챌린지를 찾아보세요!",
                                        style = WalkieTypography.Body2,
                                        color = Color(0xFF808080)
                                    )
                                }
                            }
                        }


                    } ?: run {
                        WalkieCircularProgressIndicator(Modifier.fillParentMaxWidth())
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFF7F7F7))
                )

                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "챌린지에 도전해보세요!",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "많이 도전하는 챌린지",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(11.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(13.dp),
                    contentPadding = PaddingValues(start = 20.dp)
                ) {
                    state.topRankChallengePreviewState.getDataOrNull()
                        ?.let { newChallengePreviews ->
                            newChallengePreviews.chunkedList(3).forEach { list ->
                                item {
                                    Column() {
                                        list.forEach {
                                            ChallengeItem(
                                                Modifier.fillParentMaxWidth(0.9f),
                                                it.type.getColor(),
                                                text = it.title
                                            ) {
                                                onChallengeItemClicked(it, false)
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                        }
                                    }
                                }
                            }

                        } ?: run {
                        item {
                            WalkieCircularProgressIndicator(Modifier.fillParentMaxWidth())
                        }
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "새로 나온 챌린지",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    state.newChallengePreviewsState.getDataOrNull()
                        ?.let { challengingPreviews ->

                            if (challengingPreviews.isNotEmpty()) {
                                challengingPreviews.take(3).forEach {
                                    ChallengingItem(
                                        text = it.title,
                                        progress = it.progress!!,
                                        challengeColor = it.type.getColor(),
                                    ) {
                                        onChallengeItemClicked(it, true)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                            } else {

                            }

                        }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "유형별 챌린지",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {

                val coroutineScope = rememberCoroutineScope()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ChallengeType.values().forEachIndexed { page, challengeType ->
                        Text(
                            modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(page)
                                }
                            },
                            text = challengeType.text,
                            style = WalkieTypography.SubTitle.copy(
                                color = if (page == pagerState.currentPage) Color.Black else Color.Gray
                            ),
                        )

                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {

                    state.typedChallengePreviewsState.getDataOrNull()
                        ?.let { typedChallengePreviewsState ->
                            Column() {
                                typedChallengePreviewsState[pagerState.currentPage].forEach { challengePreview ->

                                    ChallengeItem(
                                        challengeColor = challengePreview.type.getColor(),
                                        text = challengePreview.title
                                    ) {
                                        onChallengeItemClicked(challengePreview, false)
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                        ?: run { WalkieCircularProgressIndicator(Modifier.fillParentMaxWidth()) }

                }
            }
        }

    }
}


