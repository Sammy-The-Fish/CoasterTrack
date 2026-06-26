package com.example.coastertrack.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


/**
 * used as a placeholder when a picture is unavailable
 */
@Composable
fun PhotoPlaceholder(
    modifier: Modifier = Modifier,
    icon: @Composable (Modifier) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides  MaterialTheme.colorScheme.surfaceContainer) {
            icon(Modifier.fillMaxSize())
        }
    }
}