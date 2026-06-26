package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import com.example.coastertrack.ui.model.visitscreen.VisitDetailsUiModel
import com.example.coastertrack.ui.model.visitscreen.VisitDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class VisitDetailsViewModel @Inject constructor(
    private val visitDatabaseRepository: VisitDatabaseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val visitId: Int = savedStateHandle["id"]!!

    private val _isDeleteSuccess = MutableLiveData(false)

    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess

    var visitDetails = mutableStateOf<VisitDetailsUiState>(VisitDetailsUiState.Loading)
        private set

    private val format = DateFormat.getDateInstance(DateFormat.SHORT)


    init {
        getVisitDetails()
    }

    private fun getVisitDetails() {
        viewModelScope.launch {
            val visit = visitDatabaseRepository.getVisitById(visitId)
            val today = LocalDate.now()

            // converts date to type LocalDate
            val futureDate =
                Date(visit.visit.startTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val daysToEvent = ChronoUnit.DAYS.between(today, futureDate)

            visitDetails.value = VisitDetailsUiState.Success(
                details = VisitDetailsUiModel(
                    name = visit.park.name,
                    description = visit.visit.description,
                    date = format.format(Date(visit.visit.startTime)),
                    // days to event is unlikely to be larger than 2^32
                    daysToEvent = daysToEvent.toInt(),
                    id = visit.visit.id,
                    picUrl = "https://rcdb.com/${visit.park.picUrl}"
                ),
            )
        }
    }


    fun deleteVisit() {
        viewModelScope.launch {
            visitDatabaseRepository.deleteVisit(visitId)
            _isDeleteSuccess.postValue(true)
        }
    }

}