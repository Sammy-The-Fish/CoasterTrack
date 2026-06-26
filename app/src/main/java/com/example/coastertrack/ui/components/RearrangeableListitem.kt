package com.example.coastertrack.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil.compose.rememberAsyncImagePainter
import sh.calvin.reorderable.ReorderableCollectionItemScope

/**
 * function for rearrangeable list items, can only be executed in reorderable collection item scope
 */
@Composable
fun ReorderableCollectionItemScope.RearrangeableListitem(
    modifier: Modifier = Modifier,
    picUrl: String? = null,
    name: String,
    index: Int,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource
) {

    Surface(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(25.dp)),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(Color.Transparent)
        ) {
            Text(index.toString(), modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.titleLarge)
            // only show image if an image is there
            if (picUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = picUrl),
                    contentDescription = "image of $name",
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .width(100.dp)
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
                            //background being light gray to indicate it is still loading
                            MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                    contentScale = ContentScale.Crop
                )
            } else {
                PhotoPlaceholder(
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .width(100.dp)
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(25.dp))
                        .background(
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
                    .padding(10.dp),
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                modifier = Modifier
                    .padding(5.dp)
                    .draggableHandle (
                    interactionSource = interactionSource,
                    onDragStarted = {},
                    onDragStopped = {}
                ),
                onClick = {}
            ) {
                Icon(Icons.Default.DragHandle, "drag handle")
            }
        }
    }
}