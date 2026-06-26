package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.queueTimes.Ride
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.ui.model.queuetimes.NotRollercoasterUiModel
import com.example.coastertrack.ui.model.queuetimes.QueuesUIModel
import com.example.coastertrack.ui.model.queuetimes.QueuesUIState
import com.example.coastertrack.ui.model.queuetimes.RollercoasterUIModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException


/**
 * not used doint include in qriter up
 */
class QueueTimeViewModel(
    private val queueTimeRepository: QueueTimeRepository,
    private val rcdbRepository: RcdbRepository
) : ViewModel() {

    // temporary variable to hold park id
    private val id = 15

    // Mutable state to hold the current queue item
    private val _queueTimes = mutableStateOf<QueuesUIState?>(QueuesUIState.Loading)

    // immutable version exposed to UI
    val queueTimes: State<QueuesUIState?> get() = _queueTimes

    // Initialize the ViewModel
    init {
        keepFetchingQueueTimes()
    }

    // function to repeatedly get queue times
    private fun keepFetchingQueueTimes() {
        viewModelScope.launch {
            while (true) {
                fetchQueueItem()
                delay(60000)
            }
        }
    }


    // function to get queue times from API
    private fun fetchQueueItem() {
        viewModelScope.launch {
            try {
                val response = queueTimeRepository.getParkById(id)
                val rollercoastersDetails = rcdbRepository.getRollercoastersFromParkId(id)

                // add all queues from various locations to one list
                val queues = mutableListOf<Ride>()
                for (land in response.lands) {
                    for (ride in land.rides) {
                        queues.add(ride)
                    }
                }
                for (ride in response.rides) {
                    queues.add(ride)
                }

                //sort rides into list of rollercoasters and list of not rollercoasters
                val rollercoasters = mutableListOf<RollercoasterUIModel>()
                val notRollercoasters = mutableListOf<NotRollercoasterUiModel>()
                queues.forEach { ride ->
                    var found = false
                    rollercoastersDetails.forEach { rollercoaster ->
                        if (ride.id == rollercoaster.id) {
                            Log.d("ViewModel", "${ride.id}")
                            rollercoasters.add(
                                RollercoasterUIModel(
                                    name = rollercoaster.name,
                                    queue = ride.waitTime,
                                    picture = "https://rcdb.com${rollercoaster.mainPicture!!.url}",
                                    isOpen = ride.isOpen,
                                    id = ride.id
                                )
                            )
                            found = true
                        }
                    }
                    if (!found) {
                        notRollercoasters.add(
                            NotRollercoasterUiModel(
                                name = ride.name,
                                queue = ride.waitTime,
                                isOpen = ride.isOpen,
                            )
                        )
                    }
                }

                // sort by isOpen then queue times and convert to immutable list
                val sortedRollercoasters = rollercoasters.sortedWith(
                    compareBy<RollercoasterUIModel> { !it.isOpen }
                        .thenByDescending { it.queue }
                ).toList()
                val sortedNotRollercoasters = notRollercoasters.sortedWith(
                    compareBy<NotRollercoasterUiModel> { !it.isOpen }
                        .thenByDescending { it.queue }
                ).toList()

                val result = QueuesUIModel(
                    rollercoasterQueues = sortedRollercoasters,
                    notRollercoasterQueues = sortedNotRollercoasters
                )

                _queueTimes.value = QueuesUIState.Success(result)

            } catch (e: IOException) {
                // error getting response
                _queueTimes.value = QueuesUIState.Error
            }
        }
    }

    fun refreshQueueTimes() {
        _queueTimes.value = QueuesUIState.Loading
        fetchQueueItem()
    }

}
