package com.whyranoid.presentation.reusable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.R
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTheme
import com.whyranoid.presentation.theme.WalkieTypography

@Composable
fun MenuItem(
    @StringRes text: Int,
    subInfo: String?= null,
    @DrawableRes icon: Int? = null,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .padding(horizontal = 20.dp)
            .clickable(onClick = onClick)
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = stringResource(id = text),
            style = WalkieTypography.Body1_Normal
        )

        if (subInfo != null) {
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = subInfo,
                style = WalkieTypography.Body1_Normal,
                color = WalkieColor.Primary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun MenuItemPreview() {
    WalkieTheme {
        MenuItem(
            text = R.string.version_info,
            subInfo = "1.0.0",
            icon = R.drawable.ic_mobile
        )
    }
}