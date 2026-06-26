package com.example.coastertrack.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coastertrack.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckFirstTImeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,

    ) : ViewModel() {

    private val _isFirstTime: MutableState<Boolean> = mutableStateOf(false)

    val isFirstTIme: State<Boolean> = _isFirstTime


    init {
        viewModelScope.launch {
            userPreferencesRepository.getIsFirstTime().collect { data ->
                data?.let {
                    _isFirstTime.value = it
                }
                if (data == null) {
                    _isFirstTime.value = true
                }
            }
        }

    }
}