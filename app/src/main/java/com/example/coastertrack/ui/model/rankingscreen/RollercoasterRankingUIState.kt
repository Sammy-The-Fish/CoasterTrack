package com.example.coastertrack.ui.model.rankingscreen

sealed interface RollercoasterRankingUIState {
    data class Success(val list: List<RollercoasterRankingUIModel>): RollercoasterRankingUIState
    data object Error: RollercoasterRankingUIState
    data object Loading: RollercoasterRankingUIState
}