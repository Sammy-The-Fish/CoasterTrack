package com.example.coastertrack.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coastertrack.ui.components.PictureListItem
import com.example.coastertrack.ui.model.totalscreen.ListItemUiState
import com.example.coastertrack.ui.viewmodel.TotalParksViewModel

@Composable()
fun TotalParksScreen(modifier: Modifier = Modifier, navController: NavController = rememberNavController()) {
    val viewModel: TotalParksViewModel = hiltViewModel()
    val listState by viewModel.parkList
    val total by viewModel.parkCount.observeAsState()

    when(val state = listState) {
        is ListItemUiState.Loading -> {
            Scaffold { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        is ListItemUiState.Error -> {

        }
        is ListItemUiState.Success -> {
            TotalsScreen(
                modifier = modifier,
                screenTitle = {
                    Text("Total parks")
                },
                text = {
                    Text(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth(),
                        text = "$total",
                        style = TextStyle(lineHeight = 0.sp),
                        fontSize = 128.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                },
                title = {
                    Text(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        text = "total parks",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                    )
                },
                navController = navController,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                items(state.list) { item ->
                    Spacer(Modifier.height(10.dp))
                    PictureListItem(
                        name = item.name,
                        picUrl = item.picUrl,
                        onClick = {
                            navController.navigate("park/${item.id}")
                        }
                    )
                }

            }
        }

    }



}