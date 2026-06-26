package com.example.coastertrack.ui.model.rollercoasterdetails

sealed interface QueueUiState {
    data class Success(
        val queue: QueueUiModel
    ) : QueueUiState

    data object Loading : QueueUiState
    data object Error : QueueUiState
}