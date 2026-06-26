package com.example.coastertrack.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * implements [AbstractQueueTimeViewModel] getting park id from the navigation arguments
 */
@HiltViewModel
class ParkDetailsQueueTimeViewModel @Inject constructor(
    queueTimeRepository: QueueTimeRepository,
    rcdbRepository: RcdbRepository,
    savedStateHandle: SavedStateHandle,
): AbstractQueueTimeViewModel(queueTimeRepository, rcdbRepository, savedStateHandle) {

    override var id: Int?  = savedStateHandle["id"]!!


    init {
        keepFetchingQueueTimes()
    }
}