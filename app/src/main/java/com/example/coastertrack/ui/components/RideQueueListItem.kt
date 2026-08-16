package com.example.coastertrack.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coastertrack.R
import com.example.coastertrack.ui.theme.Dimens


// used to show queue times for a rollercoaster
@Composable
fun RideQueueListItem(
    name: String,
    queue: Int,
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    navController: NavController,
    //these are nullable as rides do not have a picture and do not have an on click action
    picUrl: String? = null,
    id: Int? = null,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ListItem.height)
            .clip(RoundedCornerShape(Dimens.ListItem.cornerRounding))
            .clickable(
                indication = if (id == null) null else LocalIndication.current,
                interactionSource = remember {
                    MutableInteractionSource()
                },
            ) {
                id?.let {
                    navController.navigate("rollercoaster/$it")
                }
            },
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(Dimens.ListItem.internalPadding)

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
                            //background being light grey to indicate it is still loading
                            MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = name,
                modifier = Modifier
                    .weight(4f)
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.titleLarge
            )

//            // add a spacer if no image to ensure the layout is the same
//            if (picUrl == null) {
//                Spacer(modifier = Modifier.width(100.dp))
//            }

            Column(
                modifier = modifier
                    .width(100.dp)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_access_time_24),
                    contentDescription = "clock symbol"
                )
                Text(
                    text = if (isOpen) "$queue min" else "closed",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }

}

