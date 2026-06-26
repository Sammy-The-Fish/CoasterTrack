package com.example.coastertrack.ui.model.rollercoasterdetails

sealed interface RollercoasterUiState {
    data class Success(
        val details: RollercoasterDetailsUiModel
    ): RollercoasterUiState
    data object Loading: RollercoasterUiState
    data object Error: RollercoasterUiState
}