package com.example.coastertrack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun RollercoasterDetails(
    statistic1: @Composable () -> Unit,
    statistic2: @Composable () -> Unit,
    statistic3: @Composable () -> Unit,
    statistic4: @Composable () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                statistic1()
                statistic3()
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                statistic2()
                statistic4()
            }
        }
    }
}

