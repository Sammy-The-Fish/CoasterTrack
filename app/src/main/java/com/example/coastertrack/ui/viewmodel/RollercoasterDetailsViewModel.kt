package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.queueTimes.Ride
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.domain.model.RollercoasterEntity
import com.example.coastertrack.domain.usecases.GetRollercoasterDetailsUseCase
import com.example.coastertrack.ui.mappers.toLocalEntity
import com.example.coastertrack.ui.model.rollercoasterdetails.Picture
import com.example.coastertrack.ui.model.rollercoasterdetails.QueueUiModel
import com.example.coastertrack.ui.model.rollercoasterdetails.QueueUiState
import com.example.coastertrack.ui.model.rollercoasterdetails.RollercoasterDetailsUiModel
import com.example.coastertrack.ui.model.rollercoasterdetails.RollercoasterUiState
import com.example.coastertrack.ui.model.rollercoasterdetails.Statistic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

/**
 * write up for rollercoaster details screen
 */
@HiltViewModel
class RollercoasterDetailsViewModel @Inject constructor(
    private val queueTimeRepository: QueueTimeRepository,
    private val getRollercoasterDetailsUseCase: GetRollercoasterDetailsUseCase,
    private val rollercoasterDatabaseRepository: RollercoasterDatabaseRepository,
    private val parkDatabaseRepository: ParkDatabaseRepository,
    private val rcdbRepository: RcdbRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val id: Int? = savedStateHandle["id"]

    var rollercoasterDetails: MutableState<RollercoasterUiState> =
        mutableStateOf(RollercoasterUiState.Loading)
        private set

    var queueTime = mutableStateOf<QueueUiState>(QueueUiState.Loading)
        private set


    var hasRidden: MutableState<Boolean?> = mutableStateOf(null)

    private var rollercoasterData: RollercoasterEntity? = null

    init {
        getRollercoasterDetails()
    }

    // gets rollercoaster queue time from API
    private fun getQueueTime() {
        // as queue times can only be obtained from the API on a park
        // by park basis the park ID must be obtained
        val parkId = (rollercoasterDetails.value as? RollercoasterUiState.Success)?.details?.parkId


        if (parkId == null) {
            queueTime.value = QueueUiState.Error
            return
        }

        viewModelScope.launch {
            try {
                val response = queueTimeRepository.getParkById(parkId)

                var item: Ride? = null
                response.lands.forEach { land ->
                    land.rides.forEach {
                        if (it.id == id) {
                            item = it
                        }
                    }

                }
                response.rides.forEach {
                    if (it.id == id) {
                        item = it
                    }
                }
                queueTime.value = if (item != null) QueueUiState.Success(
                    QueueUiModel(
                        queueTime = item!!.waitTime, isOpen = item!!.isOpen
                    )
                ) else QueueUiState.Error

            } catch (e: IOException) {
                queueTime.value = QueueUiState.Error
            }
        }
    }

    // obtains rollercoaster details from API
    private fun getRollercoasterDetails() {
        if (id == null) {
            rollercoasterDetails.value = RollercoasterUiState.Error
        } else {
            viewModelScope.launch {
                try {
                    val response = getRollercoasterDetailsUseCase.execute(id)


                    if (response != null) {
                        rollercoasterData = response
                        rollercoasterDetails.value = RollercoasterUiState.Success(
                            RollercoasterDetailsUiModel(name = response.name,
                                id = response.id,
                                parkId = response.parkID,
                                length = response.length,
                                inversions = response.inversions,
                                height = response.height,
                                speed = response.speed,
                                statistics = response.statistics.map { item ->
                                    Statistic(
                                        item.name, item.value
                                    )
                                },
                                pictures = response.picture.map { item ->
                                    Picture(
                                        id = item.id,
                                        name = item.name,
                                        copyName = item.copyName,
                                        copyDate = item.copyDate,
                                        url = "https://rcdb.com/${item.url}"
                                    )
                                })
                        )
                        getQueueTime()
                        checkIfOnRideList()
                    } else rollercoasterDetails.value = RollercoasterUiState.Error
                } catch (e: IOException) {
                    rollercoasterDetails.value = RollercoasterUiState.Error
                }
            }
        }
    }

    // checks if rollercoaster is on database
    private suspend fun checkIfOnRideList() {
        // as this is called at the end of the getRollercoasterDetails function, rollercoasterData will never be null
        if (rollercoasterData == null) return

        hasRidden.value =
            rollercoasterDatabaseRepository.countAllRollercoasterWithId(rollercoasterData!!.id) > 0
    }

    // check is park is on database
    private suspend fun checkIfParkOnDatabase(id: Int): Boolean {
        return parkDatabaseRepository.countParksWithIdWhereVisited(id) != 0
    }

    // saves the rollercoaster to the database
    fun saveRollercoasterToDatabase() {
        val time = System.currentTimeMillis()

        if (rollercoasterData != null) {
            val data = rollercoasterData!!.toLocalEntity(time)
            viewModelScope.launch {
                // the park must be added first, as rollercoaster references park in foreign key!!
                if (!checkIfParkOnDatabase(rollercoasterData!!.parkID)) {
                    val park = rcdbRepository.getParkByID(rollercoasterData!!.parkID)
                    if (park.isNotEmpty()) {
                        parkDatabaseRepository.addPark(park[0].toLocalEntity(time, true))
                    }
                }
                rollercoasterDatabaseRepository.addRollercoaster(data)
                hasRidden.value = true
            }
        }
    }

    // removes rollercoaster from the database
    fun removeRollercoasterFromDatabase() {
        if (hasRidden.value == true) {
            viewModelScope.launch {
                rollercoasterDatabaseRepository.deleteItemById(rollercoasterData!!.id)
                if (rollercoasterDatabaseRepository.getCountOfRollercoastersAtPark(rollercoasterData!!.parkID) == 0) {
                    parkDatabaseRepository.updateVisited(false, rollercoasterData!!.parkID)
                }
                hasRidden.value = false
            }
        }
    }

}
