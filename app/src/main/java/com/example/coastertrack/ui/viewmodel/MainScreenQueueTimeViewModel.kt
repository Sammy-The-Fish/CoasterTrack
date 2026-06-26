package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * implements [AbstractQueueTimeViewModel] getting the park id from the  park id datastore
 */
@HiltViewModel
class MainScreenQueueTimeViewModel @Inject constructor(
    queueTimeRepository: QueueTimeRepository,
    rcdbRepository: RcdbRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val visitDatabaseRepository: VisitDatabaseRepository,
    savedStateHandle: SavedStateHandle
) : AbstractQueueTimeViewModel(queueTimeRepository, rcdbRepository, savedStateHandle) {

    fun getID(): Flow<Int?> {
        return userPreferencesRepository.getParkId()
    }


    override var id: Int? = null

    init {
        initialiseId()
    }

    fun initialiseId() {
        val idFlow = getID()
        val date = Date()
        val startTime = getStartOfDayInMillis(date)
        val endTime = getEndOfDayInMillis(date)
        viewModelScope.launch {
            visitDatabaseRepository.getAllVisitsInTimeRange(startTime, endTime)
                .combine(idFlow) { list, preferenceId ->
                    Log.d("main screen queue", "$list")
                    Log.d("main screen quweue", "workin on this bit")
                    if (list.isNotEmpty()) {
                        Log.d("main screen queue", "should be using the other queue time")
                        list[0].parkID
                    } else {
                        preferenceId
                    }
                }.collect {
                Log.d("main screen queues", "we are here BTW!!! it: $it")
                id = it
                keepFetchingQueueTimes()
            }
        }
    }

    private fun getStartOfDayInMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d("start of daty", "start of day millis: ${calendar.timeInMillis}")
        return calendar.timeInMillis
    }

    private fun getEndOfDayInMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}