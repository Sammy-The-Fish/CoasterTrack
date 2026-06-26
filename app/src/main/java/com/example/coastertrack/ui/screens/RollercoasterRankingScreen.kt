package com.example.coastertrack.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.coastertrack.ui.components.RearrangeableListitem
import com.example.coastertrack.ui.viewmodel.RollercoasterRankingViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RollercoasterRankingScreen(
    navController: NavController
) {

    val viewModel = hiltViewModel<RollercoasterRankingViewModel>()

    val rideList by viewModel.rideList.collectAsState()


    val lazyListState = rememberLazyListState()

    val reoderableListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.changePosition(from.index, to.index)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rollercoaster Ranking") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, "go back")
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(0.dp, 5.dp, 0.dp, 0.dp)
                .fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState
        ) {
            itemsIndexed(
                items = rideList,
                key = { _, item -> item.id }
            ) { index, item ->
                ReorderableItem(
                    state = reoderableListState,
                    key = item.id
                ) {
                    val interactionSource = remember { MutableInteractionSource() }
                    RearrangeableListitem(
                        name = item.name,
                        picUrl = item.picUrl,
                        index = index + 1,
                        interactionSource = interactionSource,
                        onClick = {
                            navController.navigate("rollercoaster/${item.id}")
                        }
                    )
                }

            }
        }
    }
}



