package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.room.LocalVisitEntity
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import com.example.coastertrack.ui.mappers.toLocalEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class VisitCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val visitDatabaseRepository: VisitDatabaseRepository,
    private val parkDatabaseRepository: ParkDatabaseRepository,
    private val rcdbRepository: RcdbRepository
) : ViewModel() {
    // get park id from navigation argument
    private val parkId: Int = savedStateHandle["id"]!!


    // state to update the UI depending on if the visit was added successfully
    private val _successfullyAddedVisited =
        MutableLiveData<VisitCreationUiState>(VisitCreationUiState.Waiting)

    // immutable version for UI
    val successfullyAddedVisited: LiveData<VisitCreationUiState> = _successfullyAddedVisited


    val isDateValid = mutableStateOf(true)

    // text from date input shown to the user
    val dateInput = mutableStateOf("")
    private val date: MutableState<Date?> = mutableStateOf(null)

    /**
     * when null there is not error, when not null an error is present
     */
    val dateErrorMessage: MutableState<String?> = mutableStateOf(null)

    // gets users preferred date format
    private val dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT)


    var descriptionText = mutableStateOf("")
        private set


    /**
     * converts [input] from String to date, if in incorrect format [dateErrorMessage] is changed
     */
    fun updateTextDateInputDateInput(input: String) {
        dateInput.value = input
        try {
            date.value = dateFormat.parse(dateInput.value)
//            Log.d("visit view model", "${date.value}")
            checkDateValid(date.value!!)
        } catch (e: Exception) {
            isDateValid.value = false
            dateErrorMessage.value = "incorrect format"
        }
        if (input.isEmpty()) isDateValid.value = true
    }

    /**
     * checks if [date] is before midnight of the present day, updates [isDateValid] with result and [dateErrorMessage] with error message if invalid
     */
    private fun checkDateValid(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val time = Date(calendar.timeInMillis)
        if (date.before(time)) {
            isDateValid.value = false
            dateErrorMessage.value = "selected date in past"
        } else {
            isDateValid.value = true
        }
    }

    /**
     * updates [date] to be [input] converted to a date object
     */
    fun updateDateInput(input: Long) {
        date.value = Date(input)
        dateInput.value = dateFormat.format(date.value!!)
        checkDateValid(date.value!!)
    }

    /**
     * allows ui to update value of description text
     */
    fun updateDescriptionText(input: String) {
        descriptionText.value = input
    }

    /**
     * adds visit to database, updates value of [_successfullyAddedVisited] depending on result of function
     */
    fun createVisit() {
        if (isDateValid.value && date.value != null) {
            val visit = LocalVisitEntity(
                parkID = parkId,
                startTime = date.value!!.time,
                endTime = getTomorrow(date.value!!).time,
                description = descriptionText.value
            )
            viewModelScope.launch {
                if (parkDatabaseRepository.countParksWithId(parkId) == 0) {
                    // add park to database
                    try {
                        val response = rcdbRepository.getParkByID(parkId)
                        if (response.isNotEmpty()) {
                            val park = response[0]
                            val time = System.currentTimeMillis()
                            parkDatabaseRepository.addPark(park.toLocalEntity(time, false))
                        }
                    } catch (e: IOException) {
                        _successfullyAddedVisited.postValue(VisitCreationUiState.Error)
                        return@launch
                    }
                }
                val id = visitDatabaseRepository.addVisit(visit)
                _successfullyAddedVisited.postValue(VisitCreationUiState.Success(id))
            }

        }


    }

    /**
     * resets the creation state to [VisitCreationUiState.Waiting]
     */
    fun dismissError() {
        _successfullyAddedVisited.postValue(VisitCreationUiState.Waiting)
    }

    /**
     * gets last millisecond of [date]
     */
    private fun getTomorrow(date: Date): Date {
        val calender = Calendar.getInstance()
        calender.time = date
        calender.add(Calendar.DAY_OF_YEAR, 1)
        return calender.time
    }
}

/**
 * state to represent success or failure of adding a [LocalVisitEntity] to the database
 */
sealed interface VisitCreationUiState {
    /**
     * a visit is currently being added
     */
    data object Loading : VisitCreationUiState

    /**
     * an [IOException] has occurred while adding the visit
     */
    data object Error : VisitCreationUiState

    /**
     * the visit has successfully been added to the database
     */
    data class Success(val id: Long) : VisitCreationUiState

    /**
     * the user has not added the visit to the database
     */
    data object Waiting : VisitCreationUiState
}



