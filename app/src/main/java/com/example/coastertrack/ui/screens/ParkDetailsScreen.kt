package com.example.coastertrack.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.coastertrack.ui.model.parkdetails.ParkDetailsUiState
import com.example.coastertrack.ui.viewmodel.ParkDetailsQueueTimeViewModel
import com.example.coastertrack.ui.viewmodel.ParkDetailsViewModel

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ParkDetailsScreen(
//    modifier: Modifier = Modifier,
//    navController: NavController
//) {
//    val parkDetailsViewModel: ParkDetailsViewModel = hiltViewModel()
//    val queueTimeViewModel: ParkDetailsQueueTimeViewModel = hiltViewModel()
//    val context = LocalContext.current
//    val isSelected by parkDetailsViewModel.isSelected
//    val selectedParkDetails by parkDetailsViewModel.selectedParkDetails
//    parkDetailsViewModel.checkSelected(context)
//
//
//    val tabItems = listOf("rollercoasters", "rides")
//    var selectedTabIndex by remember { mutableIntStateOf(0) }
//
//    //creates pager that is size of tabItems
//    val pagerState = rememberPagerState {
//        tabItems.size
//    }
//
//
//    val scope = rememberCoroutineScope()
//
//
//    //when pagerState changes, selected tab index should be updated to match
//    LaunchedEffect(pagerState.currentPage, pagerState.currentPage) {
//        selectedTabIndex = pagerState.currentPage
//    }
//
//
//    // get queue times from view model
//    val queues by queueTimeViewModel.queueTimes
//
//
//    var showDialog by remember { mutableStateOf(false) }
//
//    val details by parkDetailsViewModel.parkDetails
//    when (val state = details) {
//        is ParkDetailsUiState.Success -> {
//            Column {
//                Scaffold(
//                    floatingActionButton = {
//                        if (isSelected) {
//                            FloatingActionButton(
//                                onClick = {
//                                },
//
//                                ) {
//                                Icon(Icons.Default.Star, contentDescription = "change favourite")
//                            }
//                        } else {
//                            FloatingActionButton(
//                                onClick = {
//                                    showDialog = true
//                                    Log.d("park details screen", "you clicked the fab didnt you")
//                                },
//                                contentColor = MaterialTheme.colorScheme.primary,
//                                containerColor = MaterialTheme.colorScheme.surface
//                            ) {
//                                Icon(
//                                    Icons.Outlined.StarOutline,
//                                    contentDescription = "change favourite"
//                                )
//                            }
//                        }
//                    },
//                    topBar = {
//                        TopAppBar(
//                            title = {
//                                Text(text = "Park Details")
//                            },
//                            navigationIcon = {
//                                IconButton(
//                                    onClick = {
//                                        navController.popBackStack()
//                                    },
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                        contentDescription = "back icon"
//                                    )
//                                }
//                            }
//                        )
//                    }
//                ) { paddingValues ->
//                    LazyDetailsScreen(
//                        modifier = Modifier.padding(paddingValues),
//                        name = { Text(state.details.name) },
//                        photo = {
//                            AsyncImage(
//                                modifier = Modifier.fillMaxWidth(),
//                                model = state.details.picUrl,
//                                contentDescription = "thorpe park",
//                                contentScale = ContentScale.Crop
//                            )
//                        },
//                    ) {
//
//                        item() {
//                            // show different content depending on whether queues have loaded or not
//                            TabRow(selectedTabIndex = selectedTabIndex) {
//                                tabItems.forEachIndexed { index, item ->
//                                    Tab(
//                                        selected = index == selectedTabIndex,
//                                        onClick = {
//                                            // when tab changes update selected Tab index and pager
//                                            scope.launch {
//                                                pagerState.animateScrollToPage(index)
//                                            }
//                                            selectedTabIndex = index
//                                        },
//                                        text = {
//                                            Text(text = item)
//                                        }
//                                    )
//                                }
//                            }
//                        }
//
//                        // allows user to swipe left and right to change screen
//                        HorizontalPager(
//                            state = pagerState,
//                            Modifier
//                                .fillMaxWidth()
//                                // fill up remaining parts of screen
//                                .weight(1f)
//                        ) { index ->
//                            val items = queuesUIState.queues
//                            Box(
//                                contentAlignment = Alignment.TopCenter,
//                                modifier = Modifier.fillMaxSize()
//                            ) {
//                                LazyColumn(
//                                    verticalArrangement = Arrangement.Top,
//                                    state = lazyListState
//                                ) {
//                                    if (index == 0) {
//                                        items(items.rollercoasterQueues) { ride ->
//                                            RideQueueListItem(
//                                                name = ride.name,
//                                                queue = ride.queue,
//                                                picUrl = ride.picture,
//                                                isOpen = ride.isOpen,
//                                                modifier = Modifier.padding(0.dp, 5.dp),
//                                                navController = navController,
//                                                id = ride.id
//                                            )
//                                        }
//                                    } else {
//                                        items(items.notRollercoasterQueues) { ride ->
//                                            RideQueueListItem(
//                                                name = ride.name,
//                                                queue = ride.queue,
//                                                isOpen = ride.isOpen,
//                                                modifier = Modifier.padding(0.dp, 5.dp),
//                                                navController = navController,
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//
//                    }
//                    if (selectedParkDetails is ParkDetailsUiState.Success) {
//                        ChangingFavouriteDialogue(
//                            showDialog = showDialog,
//                            onDismiss = { showDialog = false },
//                            onConfirm = {
//                                parkDetailsViewModel.changeFavourite(context = context)
//                                showDialog = false
//                            },
//                            currentFavourite = (selectedParkDetails as ParkDetailsUiState.Success).details.name,
//                            newFavourite = state.details.name
//                        )
//                    }
//                }
//
//                is ParkDetailsUiState.Loading -> {
//                Scaffold { paddingValues ->
//                    Box(
//                        modifier = Modifier
//                            .padding(paddingValues = paddingValues)
//                            .fillMaxSize(),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        LinearProgressIndicator()
//                    }
//                }
//            }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val parkDetailsViewModel: ParkDetailsViewModel = hiltViewModel()
    val queueTimeViewModel: ParkDetailsQueueTimeViewModel = hiltViewModel()
    val context = LocalContext.current
    val isSelected by parkDetailsViewModel.isSelected
    val selectedParkDetails by parkDetailsViewModel.selectedParkDetails



    var showDialog by remember { mutableStateOf(false) }

    val details by parkDetailsViewModel.parkDetails
    when(val state = details) {
        is ParkDetailsUiState.Success -> {
            Column {
                DetailsScreen(
                    name = { Text(state.details.name) },
                    photo = {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = state.details.picUrl,
                            contentDescription = "thorpe park",
                            contentScale = ContentScale.Crop
                        )
                    },
                    floatingActionButton = {
                                                if (isSelected) {
                            FloatingActionButton(
                                onClick = {
                                },

                                ) {
                                Icon(Icons.Default.Star, contentDescription = "change favourite")
                            }
                        } else {
                            FloatingActionButton(
                                onClick = {
                                    showDialog = true
                                    Log.d("park details screen", "you clicked the fab didnt you")
                                },
                                contentColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                Icon(
                                    Icons.Outlined.StarOutline,
                                    contentDescription = "change favourite"
                                )
                            }
                        }

                    },
                    modifier = modifier,
                    topAppBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Park Details")
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
                    QueueTimeScreen(
                        modifier = Modifier.weight(1f),
                        navController = navController,
                        queueTimeViewModel = queueTimeViewModel
                    )
                }
            }
            if (selectedParkDetails is ParkDetailsUiState.Success) {
                ChangingFavouriteDialogue(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        parkDetailsViewModel.changeFavourite(context = context)
                        showDialog = false
                    },
                    currentFavourite = (selectedParkDetails as ParkDetailsUiState.Success).details.name,
                    newFavourite = state.details.name
                )
            }
        }
        is ParkDetailsUiState.Loading -> {
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
    }
}


@Composable
fun ChangingFavouriteDialogue(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    currentFavourite: String,
    newFavourite: String
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = "confirm")
                }
            },
            title = {
                Text(text = "Change favourite?")
            },
            text = {
                Text(text = "are you sure you want to change your favourite from $currentFavourite to $newFavourite")
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "cancel")
                }
            },
        )
    }
}


@Composable
fun LazyColumnWithHorizontalPagerInside() {
    // Outer LazyColumn
    LazyColumn {
        // Add regular items in the outer LazyColumn
        items(5) { index ->
            Text(text = "Outer LazyColumn Item $index")
        }

        // Add the HorizontalPager as an item in the LazyColumn
        item {
            val pagerState = rememberPagerState {
                3
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                // Inner LazyColumn inside each page of HorizontalPager
                LazyColumn {
                    items(10) { itemIndex ->
                        Text(text = "Page $page, Item $itemIndex")
                    }
                }
            }
        }

        // Add more items after the HorizontalPager in the outer LazyColumn
        items(5) { index ->
            Text(text = "Outer LazyColumn Item $index after Pager")
        }
    }
}

@Preview
@Composable
fun PreviewLazyColumnWithHorizontalPagerInside() {
    LazyColumnWithHorizontalPagerInside()
}