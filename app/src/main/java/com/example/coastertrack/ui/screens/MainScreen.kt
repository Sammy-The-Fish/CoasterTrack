package com.example.coastertrack.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coastertrack.ui.viewmodel.MainScreenQueueTimeViewModel
import com.example.coastertrack.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.launch


data class BottomAppBarItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val fullTitle: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

    // list of tabs on bottom app bar
    val bottomAppBarItems = listOf(
        BottomAppBarItem(
            title = "Queues",
            unselectedIcon = Icons.Default.Schedule,
            selectedIcon = Icons.Filled.Schedule,
            fullTitle = "Queue time"
        ),
        BottomAppBarItem(
            title = "Parks",
            unselectedIcon = Icons.Default.Search,
            selectedIcon = Icons.Filled.Search,
            fullTitle = "Park Search"
        ),
        BottomAppBarItem(
            title = "Visits",
            unselectedIcon = Icons.Default.Commute,
            selectedIcon = Icons.Filled.Commute,
            fullTitle = "Visits"
        ),
        BottomAppBarItem(
            title = "Statistics",
            unselectedIcon = Icons.Default.Stars,
            selectedIcon = Icons.Filled.Stars,
            fullTitle = "Statistics"
        ),
    )
    //state to remember selected bottom bar item
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    //state to rememeber and control side drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //scope for coroutines
    val scope = rememberCoroutineScope()

    val context = LocalContext.current


    val viewModel: MainScreenViewModel = hiltViewModel()

    //Main scaffold of app
    ModalNavigationDrawer(
        //side drawer
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Queue Times",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "settings") },
                    selected = false,
                    onClick = { navController.navigate("test/${88}") },
                    icon = {
                        Icon(Icons.Default.Settings, "settings")
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "info") },
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(Icons.Default.Info, "information")
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "reset data") },
                    selected = false,
                    onClick = {
                        viewModel.resetData(context)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "reset data"
                        )
                    }
                )
            }
        },
        drawerState = drawerState,
        // only allow gestures when drawer is open
        gesturesEnabled = drawerState.isOpen
    ) {
        // content of page
        Scaffold(
            topBar = {
                TopAppBar(
                    // show tabs full title at the top of page
                    title = { Text(text = bottomAppBarItems[selectedItem].fullTitle) },
                    // button to open side bar
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // side bar opened as coroutine
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open side sheet"
                            )
                        }
                    },
                )
            },
            bottomBar = {
                //Bottom app bar
                NavigationBar {
                    // add 4 items to the bottom app bar
                    bottomAppBarItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                            },
                            icon = {
                                if (selectedItem == index) Icon(
                                    item.selectedIcon,
                                    item.title
                                ) else Icon(item.unselectedIcon, item.title)
                            },
                            label = { Text(item.title) },
                            alwaysShowLabel = true
                        )
                    }
                }
            },
            floatingActionButton = {
                // only show if selected screen is visits screen
                if (selectedItem == 2) {
                    FloatingActionButton(onClick = { navController.navigate("visit_creation_park_selection") }) {
                        Icon(
                            Icons.Default.Add,
                            "add visit"
                        )
                    }
                }
            }
        ) { innerPadding ->
            // modifier used to ensure content does not overlap with top or bottom app bars
            val paddedModifier = Modifier.padding(innerPadding)

            // different composable shown depending on what tab button is selected
            when (selectedItem) {
                0 -> {
                    val mainScreenQueueTimeViewModel: MainScreenQueueTimeViewModel = hiltViewModel()

                    QueueTimeScreen(
                        modifier = paddedModifier,
                        navController = navController,
                        queueTimeViewModel = mainScreenQueueTimeViewModel
                    )
                }

                1 -> {
                    ParkLookUpScreen(
                        modifier = paddedModifier,
                        navController = navController,
                        onItemClick = { navController.navigate("park/$it") }
                    )
                }

                2 -> {
                    VisitsScreen(modifier = paddedModifier, navController = navController)
                }

                3 -> {
                    StatisticsScreen(modifier = paddedModifier, navController = navController)
                }

                else -> {
                    Log.e("MAIN SCREEN", "selected item out of bounds")
                }
            }
        }
    }
}