package com.example.coastertrack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import com.example.coastertrack.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDetailsScreen(
    photoUrl: String,
    mainInfo: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    name: @Composable () -> Unit,
    floatingActionButton: (@Composable () -> Unit)? = null,
) {

    val robotoSerif = FontFamily(
        Font(R.font.roboto_serif_extrabold, FontWeight.ExtraBold)
    )


    val queueTimeShape = MaterialShapes.Cookie12Sided.toShape()


    val testImage = painterResource(R.drawable.stealth_up_close_2024_05_08_02)
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface))

    Box(modifier = Modifier.padding()) {


        Column(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Image(
                testImage,
                modifier = Modifier
                    .fillMaxHeight(0.4f),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            Box(
                modifier = Modifier
                    .offset(x = 0.dp, y = (-40).dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                Row {
                    Text(
                        "Hyperia",
                        fontFamily = robotoSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(15.dp, 20.dp)
                            .weight(2f)
                    )
                    Box (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(10.dp)
                            .clip(queueTimeShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ){

                    }
                }

            }

        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .minimumInteractiveComponentSize()
                .size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Uniform)),
            onClick = {},
            colors = IconButtonDefaults.filledIconButtonColors()
        ) {
            Icon(Icons.Default.Close, "close details page")
        }
    }


}


@Preview(device = "spec:width=392.7dp,height=850.9dp,dpi=440")
@Composable
fun TestScreen() {
    NewDetailsScreen(
        photoUrl = "https://rcdb.com/aaiezca",
        mainInfo = {},
        content = {},
        name = {},
    )
}