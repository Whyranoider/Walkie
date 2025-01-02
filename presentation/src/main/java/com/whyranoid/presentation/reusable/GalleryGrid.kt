package com.whyranoid.presentation.reusable

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.whyranoid.presentation.theme.WalkieColor
import com.whyranoid.presentation.theme.WalkieTypography
import com.whyranoid.presentation.util.openSettings
import com.whyranoid.presentation.viewmodel.RunningEditViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GalleryGrid(
    modifier: Modifier = Modifier,
    column: Int,
    onImageSelected: (Uri) -> Unit,
    onPermissionDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val hasPermission = checkPermission(context)

    if (hasPermission.not()) {
        PermissionDialog(
            onAccept = {
                (context as Activity).openSettings()
                onPermissionDismiss()
            },
            onDismiss = onPermissionDismiss,
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        )
    } else {
        val viewModel = koinViewModel<RunningEditViewModel>()
        val selectedImageState = viewModel.selectedState.collectAsStateWithLifecycle()
        val images = viewModel.getImages(LocalContext.current).collectAsLazyPagingItems()


        LazyVerticalGrid(
            modifier = modifier.fillMaxWidth()
                .height(400.dp),
            columns = GridCells.Fixed(column),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(images.itemCount) { index ->
                images[index]?.let { uri ->
                    GalleryImage(uri, isSelected = uri == selectedImageState.value) {
                        viewModel.select(uri)
                        onImageSelected(uri)
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryImage(
    uri: Uri,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    val expandState = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = WalkieColor.Primary,
                shape = RectangleShape,
            )
            .background(Color.Gray)
            .zIndex(if (expandState.value) 1f else 0f)
            .fillMaxSize()
            .then(modifier)
            .clickable { onClick() },
    ) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
        )
    }
}

fun checkPermission(context: Context): Boolean {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun PermissionDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "동의",
                    style = WalkieTypography.SubTitle.copy(color = WalkieColor.Primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) {
                            onAccept()
                        }
                        .padding(bottom = 20.dp)
                        .padding(horizontal = 20.dp),
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "미디어 및 파일 권한 요청", style = WalkieTypography.SubTitle,
                textAlign = TextAlign.Center,
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "사진을 업로드하기 위해서는 미디어 및 파일 접근 권한이 필요합니다.",
                style = WalkieTypography.Body1_Normal,
                textAlign = TextAlign.Center,
            )
        },
        modifier = modifier,
    )
}
