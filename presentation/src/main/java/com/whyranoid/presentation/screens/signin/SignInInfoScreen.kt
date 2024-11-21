package com.whyranoid.presentation.screens.signin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whyranoid.domain.model.account.Sex
import com.whyranoid.presentation.reusable.CircleProgressWithText
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.SignInState
import com.whyranoid.presentation.viewmodel.SignInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInInfoScreen(onSuccess: () -> Unit) {
    val viewModel = koinViewModel<SignInViewModel>()
    val signInState = viewModel.signInState.collectAsStateWithLifecycle()
    val infoState = signInState.value as SignInState.InfoState

    Surface(modifier = Modifier.background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text("알려주세요.", style = WalkieTypography.Title, modifier = Modifier.padding(top = 68.dp))
            Text("정확한 정보를 제공하기 위해 신체정보가 필요해요.", style = WalkieTypography.Body1_Normal)
            Text(
                "성별",
                style = WalkieTypography.Body1_Normal,
                modifier = Modifier.padding(top = 40.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedButton(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (infoState.sex == Sex.MALE) WalkieColor.Primary else WalkieColor.GrayDefault,
                    ),
                    onClick = { viewModel.setInfoState(infoState.copy(sex = Sex.MALE)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = buttonColors(
                        backgroundColor = if (infoState.sex == Sex.MALE) {
                            WalkieColor.Primary.copy(
                                alpha = 0.3f,
                            )
                        } else {
                            Color.White
                        },
                    ),
                ) {
                    Text(
                        "남",
                        style = WalkieTypography.Body1_ExtraBold,
                        color = if (infoState.sex == Sex.MALE) WalkieColor.Primary else WalkieColor.GrayDefault,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (infoState.sex == Sex.FEMALE) WalkieColor.Primary else WalkieColor.GrayDefault,
                    ),
                    onClick = { viewModel.setInfoState(infoState.copy(sex = Sex.FEMALE)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = buttonColors(
                        backgroundColor = if (infoState.sex == Sex.FEMALE) {
                            WalkieColor.Primary.copy(
                                alpha = 0.3f,
                            )
                        } else {
                            Color.White
                        },
                    ),
                ) {
                    Text(
                        "여",
                        style = WalkieTypography.Body1_ExtraBold,
                        color = if (infoState.sex == Sex.FEMALE) WalkieColor.Primary else WalkieColor.GrayDefault,
                    )
                }
            }
            Text(
                "키(cm)",
                style = WalkieTypography.Body1_Normal,
                modifier = Modifier.padding(top = 20.dp),
            )
            BasicTextField(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = WalkieColor.GrayDefault,
                        shape = RoundedCornerShape(12.dp),
                    ),
                value = infoState.height?.let { it.toString() } ?: "",
                onValueChange = { text ->
                    text.toIntOrNull()?.let { height ->
                        viewModel.setInfoState(infoState.copy(height = height))
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
            ) { textField ->
                Row(
                    Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    textField()
                }
            }
            Text(
                "몸무게(kg)",
                style = WalkieTypography.Body1_Normal,
                modifier = Modifier.padding(top = 20.dp),
            )
            BasicTextField(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .border(
                        width = 1.dp,
                        color = WalkieColor.GrayDefault,
                        shape = RoundedCornerShape(12.dp),
                    ),
                value = infoState.weight?.let { it.toString() } ?: "",
                onValueChange = { text ->
                    text.toIntOrNull()?.let { weight ->
                        viewModel.setInfoState(infoState.copy(weight = weight))
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
            ) { textField ->
                Row(
                    Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    textField()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val enable =
                infoState.sex != null && infoState.height != null && infoState.weight != null
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    onSuccess()
                },
                contentPadding = PaddingValues(vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = enable,
                colors = buttonColors(backgroundColor = WalkieColor.Primary),
                elevation = null,
            ) {
                Text("회원가입 완료", color = Color.White)
            }
        }
    }
    if (infoState.isProgress) CircleProgressWithText(text = "로딩 중")
}

@Preview
@Composable
fun PreviewInfoState() {
    WalkieTheme {
        SignInInfoScreen {
        }
    }
}
