package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.preferences.MeasurementSystem
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import com.example.coastertrack.ui.model.parklookup.ParkUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val visitDatabaseRepository: VisitDatabaseRepository,
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository,
    private val parkDatabaseRepository: ParkDatabaseRepository,
    private val rcdbRepository: RcdbRepository
): ViewModel() {

    var selectedMeasurementSystem = mutableStateOf(MeasurementSystem.DEFAULT)
       private set

    var usesFeet = mutableStateOf(false)
        private set

    val parkId = mutableStateOf<Int?>(null)
    val parkUIModel = mutableStateOf<ParkUIModel?>(null)

    init {
        viewModelScope.launch {
            userPreferencesRepository.getMeasurementSystem().collect { it ->
                selectedMeasurementSystem.value = it
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.getPrefersFeet().collect {
                usesFeet.value = it ?: false
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.getParkId().collect {
                parkId.value = it
                if (it != null) {
                    val parkData = rcdbRepository.getParkByID(it)[0]
                    parkUIModel.value = ParkUIModel(
                        name = parkData.name,
                        id = it,
                        pic = "https://rcdb.com/" + parkData.mainPicture?.url
                    )
                }
            }
        }

    }


    fun setPark(id: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setParkId(id)
        }
    }




    fun onSelectedMeasurementChange(value: MeasurementSystem) {
        viewModelScope.launch {
            userPreferencesRepository.setMeasurementSystem(value)
        }
    }

    fun onUsesFeetChange(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setPrefersFeet(value)
        }
    }

    fun resetData() {
        viewModelScope.launch {
            // visits and rollercoasters must be deleted first as they reference parks in foreign keys
            visitDatabaseRepository.deleteAllVisits()
            rollercoasterDatabaseRepository.deleteAllItems()
            parkDatabaseRepository.deleteAllParks()
            userPreferencesRepository.setParkId(2)
            userPreferencesRepository.setIsFirstTime(true)
        }
    }

}