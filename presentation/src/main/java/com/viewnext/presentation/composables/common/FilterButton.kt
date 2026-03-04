package com.viewnext.presentation.composables.common

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FilterButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember {
        androidx.compose.runtime.mutableStateOf<Job?>(null)
    }

    IconButton(
        onClick = {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(300)
                onClick()
            }
        }
    ) {
        content()
    }
}
