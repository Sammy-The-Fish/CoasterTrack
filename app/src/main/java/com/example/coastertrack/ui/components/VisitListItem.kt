package com.example.coastertrack.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.text.DateFormat
import java.util.Date


@Composable
fun VisitListItem(
    modifier: Modifier = Modifier,
    name: String,
    picUrl: String?,
    startTime: Date,
    endTime: Date? = null,
    onClick: () -> Unit = {}
) {

    val format = DateFormat.getDateInstance(DateFormat.SHORT)

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
                ) { modifier ->
                    Icon(
                        imageVector = Icons.Default.Landscape,
                        contentDescription = "no image",
                        modifier = modifier
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = format.format(startTime),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun testVisitItem() {
    VisitListItem(
        name = "test",
        picUrl = "https://rcdb.com/aagxmaa",
        startTime = Date(1737814537)
    )
}