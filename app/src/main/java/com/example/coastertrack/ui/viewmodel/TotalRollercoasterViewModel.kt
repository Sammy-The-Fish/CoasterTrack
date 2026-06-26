package com.example.coastertrack.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.ui.model.totalscreen.ListItem
import com.example.coastertrack.ui.model.totalscreen.ListItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TotalRollercoasterViewModel @Inject constructor(
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository
) : ViewModel() {

    var rollercoasterList = mutableStateOf<ListItemUiState>(ListItemUiState.Loading)
        private set

    private var _coasterCount = MutableLiveData<Int>()

    val coasterCount: LiveData<Int> = _coasterCount

    init {
        getRollercoasterTotalList()
        getCoasterCount()
    }


    private fun getRollercoasterTotalList() {
        viewModelScope.launch {
            try {
                rollercoasterDatabaseRepository.getAllRollercoastersByDate().collect { list ->
                    rollercoasterList = mutableStateOf(ListItemUiState.Success(list.map { item ->
                        ListItem(
                            name = item.name,
                            id = item.rideId,
                            picUrl = "https://rcdb.com/${item.pictureUrl}"
                        )
                    }))
                }
            } catch (e: Error) {
                rollercoasterList = mutableStateOf(ListItemUiState.Error)
            }

        }
    }

    private fun getCoasterCount() {
        viewModelScope.launch {
            rollercoasterDatabaseRepository.countRollercoasters().collect { count ->
                Log.d("stats", "coaster count is $count")
                _coasterCount.value = count
            }
        }
    }

}