package com.example.coastertrack.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.data.repository.VisitDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DONT INCLUDE IN WRITE UP NO POINT!!!!!!!!!!!!!!!!
 */
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val parkDatabaseRepository: ParkDatabaseRepository,
    private val visitDatabaseRepository: VisitDatabaseRepository
) : ViewModel() {
    fun resetData(context: Context) {
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