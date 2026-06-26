package com.example.coastertrack.ui.model.queuetimes


data class QueuesUIModel(
    val rollercoasterQueues: List<RollercoasterUIModel>,
    val notRollercoasterQueues: List<NotRollercoasterUiModel>
)