package com.example.coastertrack.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.coastertrack.R
import com.example.coastertrack.ui.components.RollercoasterDetails
import com.example.coastertrack.ui.screens.MainStatistic
import kotlinx.coroutines.launch

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


    Scaffold (
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {}
            ) {
                Text(text = "Ridden")
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add rollercoaster to ride list"
                )
            }
        },
        modifier = Modifier
//            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
//            .verticalScroll(rememberScrollState())
    ) { paddingValues ->


        Box(modifier = Modifier.padding().verticalScroll(rememberScrollState())) {


            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                AsyncImage(
                    "https://rcdb.com/aaiezca",
                    modifier = Modifier
                        .height(400.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )
                Box() {
                    Box(
                        modifier = Modifier

                            .offset(x = 0.dp, y = (-50).dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(color = MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .height(100.dp),
                    )
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().offset(0.dp, (-40).dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "Hyperia",
                                fontFamily = robotoSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 48.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
//                                overflow = TextOverflow.Ellipsis, //                                maxLines = 1,
                                lineHeight = 48.sp,
                                modifier = Modifier
                                    .padding(15.dp, 10.dp, 15.dp, 0.dp)
                                    .fillMaxWidth()
                                    .weight(1f)
                                //                            .weight(2f)
                            )
                            Box(
                                modifier = Modifier
                                    .padding(0.dp, 10.dp, 20.dp, 10.dp)
                                    .offset(0.dp, (-80).dp)
                                    .height(140.dp)
                                    .aspectRatio(1f)
                                    .clip(queueTimeShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                , contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        buildAnnotatedString {
                                            append("40" ?: "---")
                                            withStyle(style = SpanStyle(fontSize = 25.sp)) {
                                                append("min")
                                            }
                                        },
                                        style = MaterialTheme.typography.displayMediumEmphasized,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,

                                    )
                                }
                            }
                        }
                        if (false) {
                            Text(
                                "",
                                fontFamily = robotoSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 48.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
//                                overflow = TextOverflow.Ellipsis, //                                maxLines = 1,
                                lineHeight = 56.sp,
                                modifier = Modifier
                                    .padding(15.dp, 0.dp)
                                    .fillMaxWidth()
//                                    .weight(1f)
                                    .offset(0.dp, (-85).dp)
                                //                            .weight(2f)
                            )
                        }

                        Column(modifier = Modifier.offset(0.dp, (-80).dp)) {

                            RollercoasterDetails(
                                statistic1 = {
                                    MainStatistic(
                                        name = "Height:",
                                        value = "65",
                                        "m"
                                    )
                                },
                                statistic2 = {
                                    MainStatistic(
                                        name = "Speed:",
                                        value = "6",
                                        "km/h"
                                    )
                                },
                                statistic3 = {
                                    MainStatistic(
                                        name = "Length:",
                                        value = "56",
                                        "m"
                                    )
                                },
                                statistic4 = {
                                    MainStatistic(
                                        name = "Inversions",
                                        value = "20",
                                        ""
                                    )
                                },
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(25.dp, 0.dp)
                                    .clip(RoundedCornerShape(25.dp))
                                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                                    .padding(15.dp)
                            ) {
                                Spacer(Modifier.height(200.dp))
//                state.details.statistics.forEach { item ->
//                    Row(
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        androidx.compose.material3.Text(
//                            text = item.name + ": ",
//                            fontWeight = FontWeight.Bold
//                        )
//                        androidx.compose.material3.Text(
//                            text = item.value
//                        )
//                    }
//                }
                            }
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }


            }


//            IconButton(
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(paddingValues)
//                    .padding(10.dp)
//                    .minimumInteractiveComponentSize()
//                    .size(IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Uniform)),
//                onClick = {},
//                colors = IconButtonDefaults.filledIconButtonColors()
//            ) {
//                Icon(Icons.Default.Close, "close details page")
//            }
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
