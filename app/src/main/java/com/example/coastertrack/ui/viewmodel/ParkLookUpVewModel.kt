package com.example.coastertrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.QueueTimeRepository
import com.example.coastertrack.data.repository.RcdbRepository
import com.example.coastertrack.ui.model.parklookup.ParkUIModel
import com.example.coastertrack.ui.model.parklookup.PictureUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
// inject constructor using hilt
class ParkLookUpVewModel @Inject constructor(
    private val queueTimeRepository: QueueTimeRepository,
    private val rcdbRepository: RcdbRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


    private val _parks = MutableStateFlow(listOf<ParkUIModel>())


    val parks = combine(searchText, _parks) { text, parks ->
        if (text.isNotBlank()) {
            parks.filter {
                it.doesMatchSearchQuery(text)
            }
        } else {
            parks.sortedBy { it.name }
        }
    }.stateIn(
        scope = viewModelScope, // Or another coroutine scope
        started = SharingStarted.Lazily, // Starts the flow lazily
        initialValue = listOf()
    )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    init {
        // get park list on initialisation
        getParkData()

        viewModelScope.launch {
            _parks.collect { parks ->
                for (park in parks) {
                    park.pic = getParkPicture(park.id)
                }
                _parks.emit(parks)
            }
        }
    }

    private fun getParkData() {
        viewModelScope.launch {
            val response = queueTimeRepository.getParkList()
            val responseParks = mutableListOf<ParkUIModel>()
            response.forEach { company ->
                company.parks.forEach { park ->
                    responseParks.add(
                        ParkUIModel(
                            name = park.name,
                            id = park.id,
                            pic = null
                        )
                    )
                }
            }
            _parks.emit(responseParks)
        }
    }

//    private fun getParkPictures() {
//        viewModelScope.launch {
//            parks.value.forEach { park ->
//                getParkPicture(park)
//            }
//        }
//    }

    private suspend fun getParkPicture(park: Int): String? {
        try {
            val response = rcdbRepository.getParkByID(park)
            if (response.isEmpty()) {
                return null
            }

            val parkPictures = response[0]
            if (parkPictures.mainPicture != null) {
                val url = "https://rcdb.com${parkPictures.mainPicture.url}"
                return url
            } else {
                return null
            }

        } catch (e: IOException) {
            return null
        }
    }
}


