package com.example.coastertrack.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
import com.example.coastertrack.ui.model.parkdetails.ParkDetailsUiModel
import com.example.coastertrack.ui.model.parkdetails.ParkDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

/**
 * view model for park details screen
 */
@HiltViewModel
class ParkDetailsViewModel @Inject constructor(
    private val rcdbRepository: RcdbRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id: Int = savedStateHandle["id"]!!
    var parkDetails: MutableState<ParkDetailsUiState> = mutableStateOf(ParkDetailsUiState.Loading)
        private set

    private var selectedId: Int? = null

    var selectedParkDetails: MutableState<ParkDetailsUiState> =
        mutableStateOf(ParkDetailsUiState.Loading)
        private set

    init {
        getParkDetails()
        checkSelected()
    }

    var isSelected = mutableStateOf(false)


    /**
     * gets [parkDetails] from rcdb repository
     */
    private fun getParkDetails() {
        viewModelScope.launch {
            try {
                val response = rcdbRepository.getParkByID(id)
                if (response.isEmpty()) return@launch
                // returned value if successful is a list of 1 length
                val data = response[0]

                parkDetails.value = ParkDetailsUiState.Success(
                    details = ParkDetailsUiModel(
                        name = data.name,
                        id = data.id,
                        picUrl = "https://rcdb.com/${data.mainPicture?.url}"
                    )
                )
            } catch (e: IOException) {
                parkDetails.value = ParkDetailsUiState.Error
            }
        }
    }

    /**
     * checks if [id] is current favourite park
     */
    fun checkSelected() {
        val currentId = userPreferencesRepository.getParkId()
        viewModelScope.launch {
            currentId.collect {
                Log.d(
                    "park details",
                    "collecing current id: $it , id is currently $id, is selected ${isSelected.value}"
                )
                isSelected.value = it == id
                selectedId = it
                // already have info about selected park if selected park is current park
                if (!isSelected.value) {
                    getSelected()
                }
            }
        }
    }

    /**
     * gets id of current favourite park
     */
    private fun getSelected() {
        if (selectedId == null) return
        viewModelScope.launch {
            try {
                val response = rcdbRepository.getParkByID(selectedId!!)
                if (response.isEmpty()) return@launch
                // returned value if successful is a list of 1 length
                val data = response[0]

                selectedParkDetails.value = ParkDetailsUiState.Success(
                    details = ParkDetailsUiModel(
                        name = data.name,
                        id = data.id,
                        picUrl = "https://rcdb.com/${data.mainPicture?.url}"
                    )
                )
            } catch (e: IOException) {
                selectedParkDetails.value = ParkDetailsUiState.Error
            }
        }
    }

    /**
     * changes favourite id to [id]
     */
    fun changeFavourite(context: Context) {
        viewModelScope.launch {
            userPreferencesRepository.setParkId(id)
        }
    }

}