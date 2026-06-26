package com.example.coastertrack.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model for intro park selection screen
 */
@HiltViewModel
class   IntroParkSelectionViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _hasSetId = MutableLiveData(false)
    val hasSetId: LiveData<Boolean> = _hasSetId




    // saves park id to disk
    fun setParkId(id: Int) {
        viewModelScope.launch {
            userPreferencesRepository.setParkId(id)
            _hasSetId.postValue(true)
        }
    }
}