package com.example.coastertrack.ui.screens

import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coastertrack.ui.viewmodel.VisitCreationUiState
import com.example.coastertrack.ui.viewmodel.VisitCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitCreationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: VisitCreationViewModel = hiltViewModel()
) {


    val isError by viewModel.isDateValid
    val dateBoxContent by viewModel.dateInput
    val dateErrorMessage by viewModel.dateErrorMessage

    val description by viewModel.descriptionText

    val success by viewModel.successfullyAddedVisited.observeAsState(VisitCreationUiState.Waiting)

    val year = Calendar.YEAR

    var showDatePicker by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    var disableSubmitButton by remember { mutableStateOf(true) }


    val datePickerState = rememberDatePickerState(
        yearRange = year..3000,
    )


    LaunchedEffect(success) {
        when (val state = success) {
            is VisitCreationUiState.Waiting -> {
                disableSubmitButton = true
            }

            is VisitCreationUiState.Loading -> {
                disableSubmitButton = false
            }

            is VisitCreationUiState.Error -> {
                showDialog = true
            }

            is VisitCreationUiState.Success -> {
                navController.navigate("visit/${state.id}") {
                    popUpTo("visit_creation_park_selection") { inclusive = true }
                }
            }
        }
    }


    if (showDialog) {
        ErrorDialogue {
            showDialog = false
            viewModel.dismissError()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        viewModel.updateDateInput(datePickerState.selectedDateMillis!!)
                    }
                ) {
                    Text("confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Select date")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "go back") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            Row {
                OutlinedTextField(
                    value = dateBoxContent,
                    onValueChange = { viewModel.updateTextDateInputDateInput(it) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Today,
                            "select date"
                        )
                    },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable() { showDatePicker = true },
                    isError = !isError,
                    singleLine = true,
                    supportingText = {
                        if (dateErrorMessage != null && !isError) {
                            Text("$dateErrorMessage")
                        }
                    },
                    readOnly = true,
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker = true
                                    }
                                }
                            }
                        }
                )
            }

            OutlinedTextField(
                value = description,
                onValueChange = {
                    viewModel.updateDescriptionText(it)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                label = { Text("details") }
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = {
                        viewModel.createVisit()

                    },
                    enabled = disableSubmitButton
                ) {
                    Text("Create Visit")
                }


            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialogue(
    onDismiss: () -> Unit
) {
    AlertDialog(
        confirmButton = {
            TextButton(onClick = { onDismiss() }) { Text("okay") }
        },
        title = {
            Text("no internet")
        },
        text = {
            Text("internet is needed to get necesary details to create the visit")
        },
        onDismissRequest = { onDismiss() }
    )
}