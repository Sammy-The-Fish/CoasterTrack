package com.example.coastertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.example.coastertrack.ui.components.PictureListItem
import com.example.coastertrack.ui.model.parklookup.PictureUIState
import com.example.coastertrack.ui.viewmodel.ParkLookUpVewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkLookUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onItemClick: (id: Int) -> Unit = {}
) {
    val viewModel: ParkLookUpVewModel = hiltViewModel()


    val searchText by viewModel.searchText.collectAsState()
    val parks by viewModel.parks.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DockedSearchBar(
                query = searchText,
                onQueryChange = {
                    viewModel.onSearchTextChange(it)
                },
                onSearch = {
                    keyboardController?.hide()
                },
                active = false,
                onActiveChange = {/*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, top = 0.dp, end = 5.dp, bottom = 5.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "search")
                }
            ) {

            }
            HorizontalDivider(
                Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
            )


            LazyColumn(
                modifier = Modifier
                    .padding(0.dp)
                    .weight(1f), // This makes LazyColumn take the rest of the available space
                contentPadding = PaddingValues(0.dp, 10.dp)
            ) {
                items(parks) { item ->
                    when (val state = item.pic) {
                        is PictureUIState.Success -> {
                            PictureListItem(
                                name = item.name,
                                picUrl = state.url,
                                onClick = {
                                    onItemClick(item.id)
                                })
                        }

                        else -> {
                            PictureListItem(
                                name = item.name,
                                onClick = {
                                    onItemClick(item.id)
                                })
                        }
                    }
                }
            }
        }
    }
}

