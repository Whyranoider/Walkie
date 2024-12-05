package com.whyranoid.presentation.screens.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.viewmodel.SignInState
import com.whyranoid.presentation.viewmodel.SignInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInDoneScreen(onSuccess: () -> Unit) {
    val viewModel = koinViewModel<SignInViewModel>()
    val signInState = viewModel.signInState.collectAsStateWithLifecycle()
    val doneState = signInState.value as SignInState.Done

    Surface(modifier = Modifier.background(Color.White)) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                "${doneState.userName}님!\n워키에 오신 걸 환영해요.",
                style = WalkieTypography.Title,
                modifier = Modifier.padding(top = 68.dp),
            )
            Text("워키와 건강한 라이프 함께해요!", style = WalkieTypography.Body1_Normal)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    onSuccess()
                },
                contentPadding = PaddingValues(vertical = 6.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = WalkieColor.Primary),
                elevation = null,
            ) {
                Text("확인", color = Color.White)
            }
        }
    }
}
