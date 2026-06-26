package com.example.coastertrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.ui.model.rankingscreen.RollercoasterRankingUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model for rollercoaster ranking screen
 */
@HiltViewModel
class RollercoasterRankingViewModel @Inject constructor(
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository
): ViewModel() {

    private var _rideList = MutableStateFlow(listOf<RollercoasterRankingUIModel>())

    val rideList = _rideList.asStateFlow()

    init {
        getRollercoasterList()
        viewModelScope.launch {
            // updates database every time ride list changes
            _rideList.collect { list ->
                list.forEachIndexed {index, item ->
                    rollercoasterDatabaseRepository.updateRanking(index, item.id)
                }
            }
        }
    }

    // gets rollercoaster list from database
    private fun getRollercoasterList() {
        viewModelScope.launch {
            rollercoasterDatabaseRepository.getAllRollercoastersByRanking().collect {list ->
                _rideList.value = list.map {
                    RollercoasterRankingUIModel(
                        name = it.name,
                        id = it.rideId,
                        picUrl = "https://rcdb.com/${it.pictureUrl}",
                    )
                }
            }
        }
    }

    // change position of rollercoaster in the list
    fun changePosition(from: Int, to: Int) {
        _rideList.value = _rideList.value.toMutableList().apply {
            // removeAt() function return item from index it removes
            add(to, removeAt(from))
        }
    }

}