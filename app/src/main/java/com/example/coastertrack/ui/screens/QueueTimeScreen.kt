package com.example.coastertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.coastertrack.ui.components.QueueTimePageLoadingIndicator
import com.example.coastertrack.ui.components.RideQueueListItem
import com.example.coastertrack.ui.model.queuetimes.QueuesUIState
import com.example.coastertrack.ui.viewmodel.AbstractQueueTimeViewModel
import kotlinx.coroutines.launch


@Composable
fun QueueTimeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    lazyListState: LazyListState = rememberLazyListState(),
    queueTimeViewModel: AbstractQueueTimeViewModel
) {
    // items in the top tab
    val tabItems = listOf("rollercoasters", "rides")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    //creates pager that is size of tabItems
    val pagerState = rememberPagerState {
        tabItems.size
    }


    val scope = rememberCoroutineScope()


    //when pagerState changes, selected tab index should be updated to match
    LaunchedEffect(pagerState.currentPage, pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }


    // get queue times from view model
    val queues by queueTimeViewModel.queueTimes


    Column(
        modifier = modifier.padding()
    ) {
        // show different content depending on whether queues have loaded or not
        when (val state = queues) {
            is QueuesUIState.Success -> {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                // when tab changes update selected Tab index and pager
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                selectedTabIndex = index
                            },
                            text = {
                                Text(text = item)
                            }
                        )
                    }
                }
                // allows user to swipe left and right to change screen
                HorizontalPager(
                    state = pagerState,
                    Modifier
                        .fillMaxWidth()
                        // fill up remaining parts of screen
                        .weight(1f)
                ) { index ->
                    val items = state.queues
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.Top,
                            state = lazyListState
                        ) {
                            if (index == 0) {
                                items(items.rollercoasterQueues) { ride ->
                                    RideQueueListItem(
                                        name = ride.name,
                                        queue = ride.queue,
                                        picUrl = ride.picture,
                                        isOpen = ride.isOpen,
                                        modifier = Modifier.padding(0.dp, 5.dp),
                                        navController = navController,
                                        id = ride.id
                                    )
                                }
                            } else {
                                items(items.notRollercoasterQueues) { ride ->
                                    RideQueueListItem(
                                        name = ride.name,
                                        queue = ride.queue,
                                        isOpen = ride.isOpen,
                                        modifier = Modifier.padding(0.dp, 5.dp),
                                        navController = navController,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is QueuesUIState.Error -> {
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
                        OutlinedButton(
                            onClick = {
                                queueTimeViewModel.refreshQueueTimes()
                            },
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
                Text(text = "ERROR IN NETWORK")
            }

            is QueuesUIState.Loading -> {
                QueueTimePageLoadingIndicator()
            }

            null -> TODO()
        }
    }


}