package com.example.coastertrack.ui.model.totalscreen

sealed interface ListItemUiState {
    data object Error: ListItemUiState
    data object Loading: ListItemUiState
    data class Success(
        val list: List<ListItem>,
    ): ListItemUiState
}