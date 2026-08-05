package com.example.coastertrack.ui.viewmodel

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.model.preferences.MeasurementSystem
import com.example.coastertrack.data.model.queueTimes.Ride
import com.example.coastertrack.data.repository.ParkDatabaseRepository
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.data.repository.RollercoasterDatabaseRepository
import com.example.coastertrack.data.repository.UserPreferencesRepository
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
import java.util.Locale
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
    private val userPreferencesRepository: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val id: Int? = savedStateHandle["id"]

    var rollercoasterDetails: MutableState<RollercoasterUiState> =
        mutableStateOf(RollercoasterUiState.Loading)
        private set

    var queueTime = mutableStateOf<QueueUiState>(QueueUiState.Loading)
        private set

    var measurementSystem: MutableState<MeasurementSystem> =
        mutableStateOf(MeasurementSystem.DEFAULT)
    var hasRidden: MutableState<Boolean?> = mutableStateOf(null)

    var prefersFeet: MutableState<Boolean> = mutableStateOf(false)


    private var rollercoasterData: RollercoasterEntity? = null

    init {
        getRollercoasterDetails()
        getMeasurementSystem()
        getPrefersFeet()
    }


    private fun getMeasurementSystem() {
        viewModelScope.launch {
            userPreferencesRepository.getMeasurementSystem().collect {
                measurementSystem.value = it
            }
        }
    }

    private fun getPrefersFeet() {
        viewModelScope.launch {
            userPreferencesRepository.getPrefersFeet().collect {
                prefersFeet.value = it ?: false
            }
        }
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
                        queueTime = item.waitTime, isOpen = item.isOpen
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

                        val rollercoasterDetailsUiModel =
                            convertRollercoaster(
                                response,
                                measurementSystem.value,
                                prefersFeet.value
                            )

                        rollercoasterDetails.value = RollercoasterUiState.Success(
                            rollercoasterDetailsUiModel
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

    fun convertRollercoaster(
        rollercoasterEntity: RollercoasterEntity,
        measurementSystem: MeasurementSystem,
        prefersFeet: Boolean,
    ): RollercoasterDetailsUiModel {
        val METERS_TO_FEET = 3.28084
        val KMH_TO_MPH = 0.621371

        var speed: Double? = null
        var speedUnit = "kmh"

        var height: Double? = null
        var heightUnit = "m"

        var length: Double? = null
        var lengthUnit = "m"

        when (measurementSystem) {
            MeasurementSystem.DEFAULT,
            MeasurementSystem.SI -> {
                speed = rollercoasterEntity.speed
                height = rollercoasterEntity.height
                length = rollercoasterEntity.length
            }

            MeasurementSystem.UK -> {
                speed = rollercoasterEntity.speed?.times(KMH_TO_MPH)
                speedUnit = "mph"
                height = rollercoasterEntity.height
                length = rollercoasterEntity.length
            }

            MeasurementSystem.US -> {
                speed = rollercoasterEntity.speed?.times(KMH_TO_MPH)
                speedUnit = "mph"
                height = rollercoasterEntity.height?.times(METERS_TO_FEET)
                heightUnit = "ft"
                length = rollercoasterEntity.length?.times(METERS_TO_FEET)
                lengthUnit = "ft"
            }
        }

        // ensure we don't convert to US twice
        if (prefersFeet && measurementSystem != MeasurementSystem.US) {
            Log.d("rollercoaster details", "prefers feet!")
            height = height?.times(METERS_TO_FEET)
            heightUnit = "ft"
        }


        return RollercoasterDetailsUiModel(
            name = rollercoasterEntity.name,
            id = rollercoasterEntity.id,
            parkId = rollercoasterEntity.parkID,
            length = Statistic(
                name = "length",
                value = length?.let { String.format("%.1f", length) },
                unit = lengthUnit
            ),
            inversions = Statistic(
                name = "inversions",
                value = rollercoasterEntity.inversions?.toString(),
                unit = null
            ),
            height = Statistic(
                name = "height",
                value = height?.let { String.format("%.1f", height) },
                unit = heightUnit
            ),
            speed = Statistic(
                name = "speed",
                value = speed?.let { String.format("%.1f", speed) },
                unit = speedUnit
            ),
            statistics = rollercoasterEntity.statistics.map { item ->
                Statistic(
                    item.name, item.value
                )
            },
            pictures = rollercoasterEntity.picture.map { item ->
                Picture(
                    id = item.id,
                    name = item.name,
                    copyName = item.copyName,
                    copyDate = item.copyDate,
                    url = "https://rcdb.com/${item.url}"
                )
            }
        )
    }

}
