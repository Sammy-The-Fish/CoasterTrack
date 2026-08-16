package com.example.coastertrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.coastertrack.ui.theme.Dimens

/**
 * list item which contains an image and a name
 */
@Composable
fun PictureListItem(
    modifier: Modifier = Modifier,
    picUrl: String? = null,
    name: String,
    onClick: () -> Unit = {},
) {

    Surface(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ListItem.height)
            .clip(RoundedCornerShape(Dimens.ListItem.cornerRounding)),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(Color.Transparent).
            padding(Dimens.ListItem.internalPadding)
        ) {
            // only show image if an image is there
            if (picUrl != null) {
                AsyncImage(
                    model = picUrl,
                    contentDescription = "image of $name",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.3f)
                        .clip(RoundedCornerShape(Dimens.ListItem.cornerRounding))
                        .background(
                            //background being light gray to indicate it is still loading
                            MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                    contentScale = ContentScale.Crop
                )
            } else {
                PhotoPlaceholder(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.3f)
                        .clip(RoundedCornerShape(Dimens.ListItem.cornerRounding))
                        .background(
                            //background being light gray to indicate it is still loading
                            MaterialTheme.colorScheme.surfaceContainerLow
                        )
                ) {modifier ->
                    Icon(
                        imageVector = Icons.Default.Landscape,
                        contentDescription = "no image",
                        modifier = modifier
                    )
                }
            }
            Text(
                text = name,
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}