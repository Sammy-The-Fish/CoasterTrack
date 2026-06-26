package com.example.coastertrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun LazyDetailsScreen(
    modifier: Modifier = Modifier,
    name: @Composable () -> Unit,
    photo: @Composable () -> Unit,
    mainInfo: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        item {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        if (mainInfo != null) 320.dp
                        else 300.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            if (mainInfo != null) PaddingValues(0.dp, 0.dp, 0.dp, 20.dp)
                            else PaddingValues(0.dp)
                        )
                ) {
                    photo()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                // mention I changed this cos of user feedback
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color(0f, 0f, 0f, 0.5f)
                                    ),
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        CompositionLocalProvider(LocalContentColor provides Color.White) {
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.headlineLarge
                            ) {
                                name()
                            }
                        }
                    }
                }
                mainInfo?.let {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(25.dp, 0.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            mainInfo()
                        }
                    }
                }
            }
        }
        content()
    }
}