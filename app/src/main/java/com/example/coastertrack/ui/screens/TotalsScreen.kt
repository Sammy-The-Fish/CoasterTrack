package com.example.coastertrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalsScreen(
    modifier: Modifier = Modifier,
    screenTitle: @Composable () -> Unit,
    text: @Composable () -> Unit,
    navController: NavController,
    title: @Composable () -> Unit,
    backgroundColor: Color,
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { screenTitle() }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
            })
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(color = backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        title()
                        text()
                    }
                }
            }
            content()
        }
    }
}

@Preview
@Composable
fun TotalScreenPreview() {
    TotalsScreen(
        screenTitle = { Text(text = "testing") },
        text = {
            Text(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                text = "15",
                style = TextStyle(lineHeight = 0.sp),
                fontSize = 128.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center
            )
        },
        navController = rememberNavController(),
        title = {
            Text(
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                text = "total coasters",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
            )
        },
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        content = {
            
        }

    )
}