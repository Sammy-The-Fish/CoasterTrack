package com.example.coastertrack.ui.screens.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coastertrack.ui.viewmodel.EndIntroViewModel

@Preview
@Composable
fun IntroScreenThree(navController: NavController = rememberNavController()) {

    val viewModel = hiltViewModel<EndIntroViewModel>()

    Scaffold { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text("Main Features", style = MaterialTheme.typography.displayMedium)
                Detail(
                    "Check Queue Times",
                    "this app allows you to check the queue times at" +
                            " over 120 theme parks, all of them accurate"
                )
                Detail(
                    "Create visits",
                    "Create visits to ensure the app knows exactly" +
                            " what queue times you want, when you want them"
                )
                Detail(
                    "Track rollercoasters",
                    "Keep track of ridden rollercoasters and rank them into a top 10"
                )
                Detail(
                    "Find out more",
                    "find out more about specific rides, simply clicking" +
                            " on a rollercoaster can reveal large amounts of new info about it!"
                )
            }
            Button(
                onClick = {
                    viewModel.endIntro()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) { Text("get started") }
        }

    }
}

@Composable
fun Detail(name: String, description: String) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }

    }
}