package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.preferences.MeasurementSystem
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository,
    val visitDatabaseRepository: VisitDatabaseRepository,
    val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository,
    val parkDatabaseRepository: ParkDatabaseRepository
): ViewModel() {

    var selectedMeasurementSystem = mutableStateOf(MeasurementSystem.DEFAULT)
       private set

    var usesFeet = mutableStateOf(false)
        private set


    init {
        viewModelScope.launch {
            userPreferencesRepository.getMeasurementSystem().collect { it ->
                selectedMeasurementSystem.value = it
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.getPrefersFeet().collect {
                usesFeet.value = it ?: false
                Log.d("reading feeeet", "$it")
            }
        }

    }

    fun onSelectedMeasurementChange(value: MeasurementSystem) {
        viewModelScope.launch {
            userPreferencesRepository.setMeasurementSystem(value)
        }
    }

    fun onUsesFeetChange(value: Boolean) {
        viewModelScope.launch {
            Log.d("feeeet", "$value")
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