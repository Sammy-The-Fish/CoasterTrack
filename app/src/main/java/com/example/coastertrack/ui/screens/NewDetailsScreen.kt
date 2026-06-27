package com.example.coastertrack.ui.screens

import android.os.Build
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalView
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coastertrack.R
import com.example.coastertrack.ui.components.LoadingScreen
import com.example.coastertrack.ui.components.RollercoasterDetails
import com.example.coastertrack.ui.model.rollercoasterdetails.QueueUiState
import com.example.coastertrack.ui.model.rollercoasterdetails.RollercoasterUiState
import com.example.coastertrack.ui.screens.MainStatistic
import com.example.coastertrack.ui.viewmodel.RollercoasterDetailsViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDetailsScreen(
    navController: NavController,
) {

    val viewModel: RollercoasterDetailsViewModel = hiltViewModel()

    val rollerCoasterDetails by viewModel.rollercoasterDetails
//    val rollerCoasterDetails = RollercoasterUiState.Loading
    val queueTime by viewModel.queueTime
    val hasRidden by viewModel.hasRidden

    var showDialogue by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val view = LocalView.current


    var titleFirstLine by remember { mutableStateOf<String?>(null) }
    var titleRemainder by remember { mutableStateOf("") }
    var fits by remember { mutableStateOf(true) }

    val robotoSerif = FontFamily(
        Font(R.font.roboto_serif_extrabold, FontWeight.ExtraBold)
    )


    val queueTimeShape = MaterialShapes.Cookie12Sided.toShape()
    val closedShape = MaterialShapes.Triangle.toShape()

    when (val state = rollerCoasterDetails) {
        is RollercoasterUiState.Success -> {
            RemoveDialogue(
                showDialogue = showDialogue,
                onDismiss = { showDialogue = false },
                onConfirm = {
                    viewModel.removeRollercoasterFromDatabase()
                    showDialogue = false
                },
                coasterName = state.details.name
            )



            Scaffold(
                floatingActionButton = {
                    if (hasRidden != null) {
                        AnimatedVisibility(
                            visible = !hasRidden!!,
                            exit = shrinkHorizontally() + fadeOut()
                        ) {
                            ExtendedFloatingActionButton(onClick = {
                                viewModel.saveRollercoasterToDatabase()
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        "${state.details.name} added to ride list",
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                }
                                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            },
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                Text(text = "Ridden")
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "add rollercoaster to ride list"
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = hasRidden!!,
                            enter = fadeIn(tween(0))
                        ) {
                            FloatingActionButton(onClick = { showDialogue = true }) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "rollercoaster on ride list"
                                )
                            }
                        }


                    }
                },
                modifier = Modifier
//            .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
//            .verticalScroll(rememberScrollState())
            ) { paddingValues ->


                Box(
                    modifier = Modifier
                        .padding()
                        .verticalScroll(rememberScrollState())
                ) {


                    Column(
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        AsyncImage(
                            state.details.pictures[0].url,
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(0.dp, (-40).dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().offset(0.dp, (-80).dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val size = ButtonDefaults.MediumContainerHeight
                                    FilledTonalButton(
                                        onClick = { navController.popBackStack() },
                                        modifier = Modifier.heightIn(size)
                                            .padding(20.dp, 0.dp, 0.dp, 0.dp),
                                        contentPadding = ButtonDefaults.contentPaddingFor(size),
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Localized description",
                                            modifier = Modifier.size(ButtonDefaults.iconSizeFor(size)),
                                        )
                                        Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(size)))
                                        Text("Back", style = ButtonDefaults.textStyleFor(size))
                                    }
                                    when (val queueState = queueTime) {
                                        is QueueUiState.Success -> {
                                            if (queueState.queue.isOpen) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(0.dp, 10.dp, 20.dp, 10.dp)
                                                        .height(140.dp)
                                                        .aspectRatio(1f)
                                                        .clip(queueTimeShape)
                                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            buildAnnotatedString {
                                                                append(queueState.queue.queueTime.toString())
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
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(0.dp, 10.dp, 20.dp, 10.dp)
                                                        .height(140.dp)
                                                        .aspectRatio(1f)
                                                        .clip(closedShape)
                                                        .background(MaterialTheme.colorScheme.errorContainer)
                                                        .height(100.dp),
                                                    contentAlignment = Alignment.BottomCenter
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            "closed",
                                                            style = MaterialTheme.typography.displayMediumEmphasized,
                                                            fontWeight = FontWeight.Medium,
                                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                                            fontSize = 30.sp,
                                                            modifier = Modifier.padding(
                                                                0.dp,
                                                                0.dp,
                                                                0.dp,
                                                                10.dp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        is QueueUiState.Loading -> {
                                            Spacer(modifier = Modifier.height(160.dp))
                                        }
                                        is QueueUiState.Error -> {

                                        }
                                    }
                                }


                                Column(modifier = Modifier.offset(0.dp, (-95).dp)) {
                                    Text(
                                        state.details.name,

                                        fontFamily = robotoSerif,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 42.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
//                                overflow = TextOverflow.Ellipsis, //                                maxLines = 1,
                                        lineHeight = 48.sp,
                                        modifier = Modifier
                                            .padding(15.dp, 0.dp)
                                            .fillMaxWidth()
//                                    .weight(1f)
//                                            .offset(0.dp, (-85).dp)
                                        //                            .weight(2f)
                                    )

                                    RollercoasterDetails(
                                        statistic1 = {
                                            MainStatistic(
                                                name = "Height:",
                                                value = state.details.height?.toString(),
                                                "m"
                                            )
                                        },
                                        statistic2 = {
                                            MainStatistic(
                                                name = "Speed:",
                                                value = state.details.speed?.toString(),
                                                "km/h"
                                            )
                                        },
                                        statistic3 = {
                                            MainStatistic(
                                                name = "Length:",
                                                value = state.details.length?.toString(),
                                                "m"
                                            )
                                        },
                                        statistic4 = {
                                            MainStatistic(
                                                name = "Inversions",
                                                value = state.details.inversions?.toString(),
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
                                        state.details.statistics.forEach { item ->
                                            Row(
                                                horizontalArrangement = Arrangement.Start
                                            ) {
                                                Text(
                                                    text = item.name + ": ",
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = item.value
                                                )
                                            }
                                        }
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

        is RollercoasterUiState.Loading -> {
            LoadingScreen()
        }

        is RollercoasterUiState.Error -> {
            Text("somthing went wrong!")
        }
    }


}


