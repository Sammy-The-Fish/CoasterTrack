package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.ui.model.totalscreen.ListItem
import com.example.coastertrack.ui.model.totalscreen.ListItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model for total parks screen
 */
@HiltViewModel
class TotalParksViewModel @Inject constructor(
    private val parkDatabaseRepository: ParkDatabaseRepository
) : ViewModel() {


    var parkList = mutableStateOf<ListItemUiState>(ListItemUiState.Loading)
        private set

    private var _parkCount = MutableLiveData<Int>()

    val parkCount: LiveData<Int> = _parkCount

    init {
        getParkList()
        getParkCount()
    }

    // gets park lis from database
    private fun getParkList() {
        viewModelScope.launch {
            try {
                parkDatabaseRepository.getAllParksByTimeVisited().collect { list ->
                    parkList = mutableStateOf(ListItemUiState.Success(list.map { item ->
                        ListItem(
                            name = item.name,
                            id = item.parkID,
                            picUrl = "https://rcdb.com/${item.picUrl}"
                        )
                    }))
                }
            } catch (e: Error) {
                parkList = mutableStateOf(ListItemUiState.Error)
            }

        }
    }

    // gets total number of parks from database
    private fun getParkCount() {
        viewModelScope.launch {
            parkDatabaseRepository.countParks().collect { count ->
                Log.d("stats", "coaster count is $count")
                _parkCount.value = count
            }
        }
    }
}