package com.example.coastertrack.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coastertrack.ui.components.VisitListItem
import com.example.coastertrack.ui.viewmodel.VisitScreenViewModel

@Composable
fun VisitsScreen(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: VisitScreenViewModel = hiltViewModel()

    val visits by viewModel.visits

    if (visits.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
        ) {
            items(visits) { item ->
                VisitListItem(
                    name = item.parkName,
                    picUrl = item.parkPicUrl,
                    startTime = item.startTime,
                    onClick = {
                        navController.navigate("visit/${item.id}")
                    }
                )
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "press + to create a visit",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }

}


