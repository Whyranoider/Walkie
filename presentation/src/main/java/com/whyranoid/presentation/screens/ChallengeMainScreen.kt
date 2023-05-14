package com.whyranoid.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.whyranoid.presentation.component.ChallengeItem
import com.whyranoid.presentation.component.ChallengingItem
import com.whyranoid.presentation.util.chunkedList
import com.whyranoid.presentation.viewmodel.ChallengeState
import com.whyranoid.presentation.viewmodel.ChallengeViewModel
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ChallengeMainScreen(
    navController: NavController
) {

    val viewModel = koinViewModel<ChallengeViewModel>()

    val state by viewModel.collectAsState()

    ChallengeMainContent(
        state,
        onChallengeItemClicked = {

        },
        onExpandButtonClicked = {

        }
    )

}

@Composable
fun ChallengeMainContent(
    state: ChallengeState,
    onChallengeItemClicked: () -> Unit = {},
    onExpandButtonClicked: () -> Unit = {},
) {

    Scaffold(
        Modifier.padding(horizontal = 20.dp)
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues)) {

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "신규",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(13.dp)
                ) {

                    if (state.newChallengePreviews.isNotEmpty()) {
                        state.newChallengePreviews.forEach {
                            item {
                                ChallengeItem(
                                    Modifier.fillParentMaxWidth(0.9f),
                                    text = it.title
                                ) {
                                    onChallengeItemClicked()
                                }
                            }
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(44.dp))
            }

            item {
                Text(
                    text = "도전중인 챌린지",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (state.challengingPreviews.isNotEmpty()) {
                        state.challengingPreviews.take(4).forEach {
                            ChallengingItem(
                                text = it.title,
                                progress = it.progress!!,
                                imageUrl = it.badgeImageUrl,
                            ) {
                                onChallengeItemClicked()
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        IconButton(
                            onClick = { onExpandButtonClicked() }) {
                            Icon(
                                imageVector = Icons.Rounded.ExpandMore,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp),
                                contentDescription = "도전중인 챌린지 더보기"
                            )
                        }

                    } else {
                        Box(
                            modifier = Modifier.fillParentMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Text(
                    text = "다른 챌린지도 도전해보세요!",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                Text(
                    text = "인기",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    if (state.newChallengePreviews.isNotEmpty()) {
                        state.newChallengePreviews.chunkedList(3).forEach { list ->
                            item {
                                Column() {
                                    list.forEach {
                                        ChallengeItem(
                                            Modifier.fillParentMaxWidth(0.9f),
                                            text = it.title
                                        ) {
                                            onChallengeItemClicked()
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }

                    } else {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
