package com.whyranoid.presentation.screens.setting

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.whyranoid.presentation.R
import com.whyranoid.presentation.reusable.MenuItem
import com.whyranoid.presentation.screens.Screen
import com.whyranoid.presentation.screens.mypage.editprofile.UserInfoUiState
import com.whyranoid.presentation.theme.SystemColor
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(navHostController: NavHostController) {
    val viewModel = koinViewModel<SettingViewModel>()
    val user = viewModel.userInfoUiState.collectAsState()

    val scrollState = rememberScrollState()

    user.value?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            TopAppBar { navHostController.popBackStack() }
            ProfileSection(it) {
                navHostController.navigate(Screen.EditProfileScreen.route) }
            SettingsList()
        }
    }
}

@Composable
fun TopAppBar(
    onBackClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable { onBackClicked() }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "설정", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun ProfileSection(
    user: UserInfoUiState,
    onProfileEditClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.profileUrl ?: R.drawable.ic_default_profile,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.nickname,
                style = WalkieTypography.SubTitle
            )

            Text(
                text = user.name,
                style = WalkieTypography.Body2,
                color = SystemColor.Negative
            )
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onProfileEditClicked()
            }
            .background(WalkieColor.GrayBackground)
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.edit_profile),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.button
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun SettingsList() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(WalkieColor.GrayBackground)
    )

    GroupTitle(title = R.string.manage_app)

    val context = LocalContext.current
    val emailAddress = "lets.walkie@gmail.com"
    val emailTitle = stringResource(id = R.string.customer_service_email_title)
    val emailContent = stringResource(id = R.string.customer_service_email_content)
    val emailAppChooserTitle = stringResource(id = R.string.customer_service_email_chooser)

    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        putExtra(Intent.EXTRA_SUBJECT, emailTitle)
        putExtra(Intent.EXTRA_TEXT, emailContent)
    }

    MenuItem(
        text = R.string.customer_service,
        icon = R.drawable.ic_call,
        onClick = {
            context.startActivity(
                Intent.createChooser(emailIntent, emailAppChooserTitle)
            )
        },
    )

    MenuItem(
        text = R.string.license,
        icon = R.drawable.ic_info
    )

    MenuItem(
        text = R.string.terms_and_policy,
        icon = R.drawable.ic_book
    )

    MenuItem(
        text = R.string.version_info,
        icon = R.drawable.ic_mobile
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(WalkieColor.GrayBackground)
    )

   MenuItem(
       text = R.string.logout,
       icon = null
   )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(WalkieColor.GrayBackground)
    )

    MenuItem(
        text = R.string.delete_account,
        icon = null
    )
}

@Composable
private fun GroupTitle(
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = WalkieTypography.Body1_SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
    )
}