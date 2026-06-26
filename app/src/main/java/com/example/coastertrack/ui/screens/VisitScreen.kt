package com.example.coastertrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.coastertrack.ui.model.visitscreen.VisitDetailsUiState
import com.example.coastertrack.ui.viewmodel.VisitDetailsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VisitScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController()
) {

    val viewModel: VisitDetailsViewModel = hiltViewModel()


    val details by viewModel.visitDetails

    val isDeleteSuccess by viewModel.isDeleteSuccess.observeAsState()

    LaunchedEffect(isDeleteSuccess) {
        if (isDeleteSuccess == true) {
            navController.popBackStack()
        }
    }

    when (val state = details) {
        is VisitDetailsUiState.Success -> {
            DetailsScreen(
                name = {
                    Text(state.details.name)
                },
                photo = {
                    AsyncImage(
                        model = state.details.picUrl,
                        contentDescription = state.details.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                mainInfo = {
                    if (state.details.daysToEvent == 0) {
                        Icon(
                            Icons.Default.Celebration,
                            "visit today"

                        )
                        Spacer(modifier.width(10.dp))

                        Text("today")
                    }
                    else {
                        Icon(
                            Icons.Default.Today,
                            "calender"
                        )
                        Spacer(modifier.width(10.dp))
                        Text("${state.details.daysToEvent} days")
                    }
                },
                topAppBar = {
                    TopAppBar(
                        title = {
                            Text(text = "Visit Details")
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
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    viewModel.deleteVisit()
                                }
                            ) {
                                Icon(Icons.Default.Delete, "delete visit")
                            }
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(Icons.Default.Edit, "edit visit")
                            }
                        }
                    )
                }
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.padding(10.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(25.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Date: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = state.details.date,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Time: ",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "00:00",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }

                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(25.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Details:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.details.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

            }
        }

        VisitDetailsUiState.Error -> TODO()
        is VisitDetailsUiState.Loading -> {
            Scaffold {innerPadding ->
                Box(Modifier.padding(innerPadding))
            }
        }
    }

}

