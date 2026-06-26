package com.example.coastertrack.ui.model.visitscreen

sealed interface VisitDetailsUiState {
    data object Error: VisitDetailsUiState
    data class Success(
        val details: VisitDetailsUiModel
    ): VisitDetailsUiState
    data object Loading: VisitDetailsUiState
}