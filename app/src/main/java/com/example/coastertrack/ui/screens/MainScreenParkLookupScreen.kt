package com.example.coastertrack.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun MainScreenParkLookupScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    ParkLookUpScreen(navController = navController, modifier = modifier, onItemClick = {id ->
        navController.navigate("park/${id}")
    })
}