package com.example.coastertrack.ui.screens.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coastertrack.ui.screens.ParkLookUpScreen
import com.example.coastertrack.ui.viewmodel.IntroParkSelectionViewModel


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IntroScreenTwo(navController: NavController = rememberNavController()) {
    val viewModel = hiltViewModel<IntroParkSelectionViewModel>()

    val hasSelected by viewModel.hasSetId.observeAsState()

    LaunchedEffect(hasSelected) {
        if (hasSelected == true) {
            navController.navigate("intro_3")
        }
    }

    Scaffold { paddingValues ->
        Column {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column {
                    Text("Select your favourite park", style = MaterialTheme.typography.headlineMediumEmphasized)
                }

                Text(
                    "Select the park that should appear when you open the app, " +
                            "you can change this later in the park look up menu",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            ParkLookUpScreen(
                navController = navController,
                onItemClick = { id -> viewModel.setParkId(id) }
            )
        }
    }
}