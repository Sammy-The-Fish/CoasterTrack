package com.example.coastertrack.ui.model.parklookup

sealed interface PictureUIState{
    data class Success(
        val url: String
    ): PictureUIState
    data object Loading: PictureUIState
    data object Error: PictureUIState
    data object None: PictureUIState
}