package com.example.coastertrack.ui.model.queuetimes

sealed interface QueuesUIState {
    data class Success(
        val queues: QueuesUIModel,
    ) : QueuesUIState

    data object Error : QueuesUIState
    data object Loading : QueuesUIState
}
