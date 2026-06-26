package com.example.coastertrack.ui.model.parkdetails

interface ParkDetailsUiState {
    data class Success(
        val details: ParkDetailsUiModel
    ): ParkDetailsUiState
    data object Error: ParkDetailsUiState
    data object Loading: ParkDetailsUiState
}