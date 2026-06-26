package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import com.example.coastertrack.ui.model.visitscreen.VisitUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class   VisitScreenViewModel @Inject constructor(
    private val visitDatabaseRepository: VisitDatabaseRepository
) : ViewModel() {


    var visits = mutableStateOf((listOf<VisitUiModel>()))
        private set

    init {
        getVisits()
    }

    private fun getVisits() {
        viewModelScope.launch {
            visitDatabaseRepository.getAllVisitsWithParksByStartTime().collect { list ->
                val allVisits = list.map {
                    VisitUiModel(
                        startTime = Date(it.visit.startTime),
                        endTime = Date(it.visit.endTime),
                        id = it.visit.id,
                        parkName = it.park.name,
                        description = it.visit.description,
                        parkPicUrl = "https://rcdb.com/${it.park.picUrl}"
                    )
                }
                val now = getStartOfDay(Date())
                // remove visits if before now
                visits.value = allVisits.filter { !(it.startTime.before(now)) }
                allVisits.filter { it.startTime.before(now) }.forEach {
                    visitDatabaseRepository.deleteVisit(it.id)
                }
            }
        }
    }

    private fun getStartOfDay(date: Date): Date {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val startOfDay = localDate.atStartOfDay()
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant())
    }

}