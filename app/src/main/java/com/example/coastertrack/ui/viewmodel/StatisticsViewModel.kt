package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.room.LocalRollercoasterEntity
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.ui.model.rankingscreen.RollercoasterRankingUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model for statistics screen
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository,
    private val parkDatabaseRepository: ParkDatabaseRepository
) : ViewModel() {

    private var _coasterCount = MutableLiveData<Int>()
    private var _parkCount = MutableLiveData<Int>()

    // immutable versions exposed to the ui
    val coasterCount: LiveData<Int> = _coasterCount
    val parkCount: LiveData<Int> = _parkCount

    private var rideList = MutableStateFlow(listOf<LocalRollercoasterEntity>())


    var topRides = mutableStateOf(listOf<RollercoasterRankingUIModel>())
        private set
    var rideCount = mutableIntStateOf(0)
        private set

    var statistics = mutableStateOf(listOf<StatisticUIModel>())
        private set


    init {
        getCoasterCount()
        getParkCount()
        getRideList()
        viewModelScope.launch {
            // update statistics if database changes
            rideList.collect { list ->
                rideCount.intValue = list.size
                topRides.value = list.sortedBy { it.ranking!! }.map {
                    RollercoasterRankingUIModel(
                        name = it.name,
                        id = it.rideId,
                        picUrl = "https://rcdb.com/${it.pictureUrl}",
                    )
                }
                statistics.value = statisticCalculators.map {
                    StatisticUIModel(
                        name = it.name,
                        value = it.updateFunction(list),
                        unit = it.unit
                    )
                }
            }
        }

    }

    // gets total rollercoaster from database
    private fun getCoasterCount() {
        viewModelScope.launch {
            rollercoasterDatabaseRepository.countRollercoasters().collect { count ->
                _coasterCount.value = count
            }
        }
    }

    // gets total parks from database
    private fun getParkCount() {
        viewModelScope.launch {
            parkDatabaseRepository.countParks().collect { count ->
                _parkCount.value = count
            }
        }
    }

    // gets all rollercoasters from database
    private fun getRideList() {
        viewModelScope.launch {
            rollercoasterDatabaseRepository.getAllRollercoastersByDate().collect {
                rideList.value = it
            }
        }
    }
}

/**
 * list of statistics to be calculated
 */
val statisticCalculators = listOf(
    StatisticCalculator(
        name = "total ditance travelled",
        updateFunction = { rideList ->
            var total = 0.0
            rideList.forEach { ride ->
                total += ride.length ?: 0.0
            }
            String.format("%.2f", total)
        },
        unit = "m"
    ),
    StatisticCalculator(
        "height climbed", updateFunction = { rideList ->
            var total = 0.0
            rideList.forEach { ride ->
                total += ride.height ?: 0.0
            }
            String.format("%.2f", total)
        },
        unit = "m"
    ),
    StatisticCalculator("inversions completed") { rideList ->
        var total = 0
        rideList.forEach { ride ->
            total += ride.inversions ?: 0
        }
        total.toString()
    },
    StatisticCalculator("highest speed", unit = "km/h") { rideList ->
        (rideList.maxByOrNull { it.speed ?: 0.0 }?.speed ?: 0.0).toString()
    }

)

data class StatisticCalculator(
    val name: String,
    val unit: String? = null,
    val updateFunction: (rideList: List<LocalRollercoasterEntity>) -> String,
)


data class StatisticUIModel(
    val name: String,
    val unit: String? = null,
    val value: String,
)