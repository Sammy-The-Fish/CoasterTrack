package com.example.coastertrack.ui.screens

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coastertrack.data.model.preferences.MeasurementSystem
import com.example.coastertrack.ui.components.ParkSelector
import com.example.coastertrack.ui.components.PictureListItem
import com.example.coastertrack.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val radioOptions = listOf("Metric", "UK", "US")
    val firstOption = radioOptions[0]
    val lastOption = radioOptions.last()


    val viewModel = hiltViewModel<SettingsViewModel>()

    val selectedOption by viewModel.selectedMeasurementSystem

//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var useFeet by viewModel.usesFeet

    var showResetDialogue by remember { mutableStateOf(false) }


    val density = LocalDensity.current


    val selectedParkId = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>("selectedParkId", null)
        ?.collectAsState()

    val savedPark by viewModel.parkUIModel

    LaunchedEffect(selectedParkId?.value) {
        selectedParkId?.value?.let { id ->
            viewModel.setPark(id)
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 15.dp)
        ) {
            Text(
                "Favourite park",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            )
            if (savedPark == null) {
                PictureListItem(
                    picUrl = null,
                    name = "",
                    onClick = {
                        navController.navigate("park_selector")
                    }
                )
            }else {
                PictureListItem(
                    picUrl = savedPark!!.pic,
                    name = savedPark!!.name,
                    onClick = {
                        navController.navigate("park_selector")
                    }
                )
            }

            Text(
                "Measurement Units",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            )
            Column(modifier = Modifier.selectableGroup()) {
                MeasurementSystem.entries.forEachIndexed { index, system ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 1.dp)
                            .clip(
                                if (index == 0) {
                                    RoundedCornerShape(20.dp, 20.dp, 5.dp, 5.dp)
                                } else if (index == MeasurementSystem.entries.lastIndex) {
                                    RoundedCornerShape(5.dp, 5.dp, 20.dp, 20.dp)
                                } else {
                                    RoundedCornerShape(5.dp)
                                }
                            )
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .selectable(
                                selected = (system == selectedOption),
                                onClick = { viewModel.onSelectedMeasurementChange(system) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (system == selectedOption),
                            onClick = null // null recommended for accessibility with screen readers
                        )
                        Text(
                            text = system.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(
                selectedOption != MeasurementSystem.US,
                enter = slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                )  + expandVertically(),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 15.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        )
                        .clickable(onClick = {
                            viewModel.onUsesFeetChange(!useFeet)
                        })
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Use Feet for height", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = useFeet,
                        onCheckedChange = null,
                    )
                }
            }
            Text(
                "Data",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
//                    .padding(vertical = 15.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .clickable(onClick = {
                        showResetDialogue = true
                    })
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    Icons.Default.Restore,
                    "Reset data",
                )
                Text(
                    "Reset Data",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        ResetDialogue(
            showResetDialogue,
            { showResetDialogue = false },
            {
                viewModel.resetData()
            }
        )
    }
}

@Composable
fun ResetDialogue(
    showDialogue: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (showDialogue) {
        AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "Reset")
            }
        }, dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }, title = {
            Text(text = "Reset data?")
        }, text = {
            Text(text = "Ride list and visits will be reset and cannot be recovered")
        })
    }
}