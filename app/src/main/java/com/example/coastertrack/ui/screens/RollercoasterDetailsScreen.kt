package com.example.coastertrack.ui.screens


import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coastertrack.ui.components.RollercoasterDetails
import com.example.coastertrack.ui.model.rollercoasterdetails.QueueUiState
import com.example.coastertrack.ui.model.rollercoasterdetails.RollercoasterUiState
import com.example.coastertrack.ui.viewmodel.RollercoasterDetailsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollercoasterDetailsScreen(
    navController: NavController
) {
    val viewModel: RollercoasterDetailsViewModel = hiltViewModel()

    val rollerCoasterDetails by viewModel.rollercoasterDetails
    val queueTime by viewModel.queueTime
    val hasRidden by viewModel.hasRidden

    var showDialogue by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val view = LocalView.current


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
            DetailsScreen(
                snackbarHostState = snackBarHostState,
                name = {
                    Text(text = state.details.name)
                },
                photo = {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = state.details.pictures[0].url,
                        contentDescription = "picture of ${state.details.name}",
                        contentScale = ContentScale.Crop
                    )
                },
                mainInfo = {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = "queue time",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    when (val queueUiState = queueTime) {
                        is QueueUiState.Success -> {
                            Text(
                                text = if (queueUiState.queue.isOpen) "${queueUiState.queue.queueTime} min"
                                else "closed",
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        else -> {
                            Text(
                                text = "---",
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                },
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
                            }) {
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
                scrollState = rememberScrollState(),
                topAppBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Rollercoaster Details")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "back icon"
                                )
                            }
                        }
                    )
                }
            ) {
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

        is RollercoasterUiState.Loading -> {

            Scaffold { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    LinearProgressIndicator()
                }
            }

        }

        is RollercoasterUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.WifiOff,
                        contentDescription = "wifi off",
                        modifier = Modifier
                            .size(200.dp),
                        tint = MaterialTheme.colorScheme.surfaceContainer,

                        )
                    Text("No internet")
                }
            }
        }
    }

}

@Composable
fun MainStatistic(
    name: String,
    value: String?,
    unit: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(15.dp, 15.dp)
            .height(100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Text(
            text = name,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            fontSize = 15.sp
        )
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                buildAnnotatedString {
                    append(value ?: "---")
                    withStyle(style = SpanStyle(fontSize = 20.sp)) {
                        append(" $unit")
                    }
                },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
        }

    }
}

@Composable
fun RemoveDialogue(
    showDialogue: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    coasterName: String,
) {
    if (showDialogue) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "cancel")
                }
            },
            title = {
                Text(text = "remove $coasterName?")
            },
            text = {
                Text(text = "are you sure want to remove $coasterName from your ride list?")
            }
        )
    }
}
