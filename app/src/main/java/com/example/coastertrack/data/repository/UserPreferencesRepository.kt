package com.example.coastertrack.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.coastertrack.data.datastore.UserPreferenceKeys
import com.example.coastertrack.data.datastore.UserPreferences
import kotlinx.coroutines.flow.Flow

// allows UI and domain layers to interact with the user preferences system
class UserPreferencesRepository(
    private val context: Context
) {

    fun getParkId(): Flow<Int?> {
        return UserPreferences.getIntValue(intPreferencesKey(UserPreferenceKeys.PARK_ID), context)
    }

    suspend fun setParkId(value: Int) {
        UserPreferences.saveIntValue(value, intPreferencesKey(UserPreferenceKeys.PARK_ID), context)
    }


    fun getIsFirstTime(): Flow<Boolean?> {
        return UserPreferences.getBoolValue(booleanPreferencesKey(UserPreferenceKeys.IS_FIRST_TIME), context)
    }

    suspend fun setIsFirstTime(value: Boolean) {
        UserPreferences.setBoolValue(value, booleanPreferencesKey(UserPreferenceKeys.IS_FIRST_TIME), context)
    }
}