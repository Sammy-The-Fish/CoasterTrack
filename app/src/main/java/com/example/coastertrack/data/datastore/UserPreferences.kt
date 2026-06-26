package com.example.coastertrack.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

object UserPreferences {
    // name for datastore
    private const val USER_PREFERENCES_NAME = "preferences"

    // creates the new datastore using name
    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    // function to save to integer to data store
    internal suspend fun saveIntValue(
        value: Int,
        key: Preferences.Key<Int>,
        context: Context
    ) {
        context.dataStore.edit { saveData ->
            saveData[key] = value
        }
    }

    // gets integer value from datastore
    internal fun getIntValue(
        key: Preferences.Key<Int>,
        context: Context
    ): Flow<Int?> =
        context.dataStore.data
            .catch { exception ->
                when (exception) {
                    is IOException -> Log.d(
                        "user preferences helper",
                        "something went wrong :(, internet error!!"
                    )

                    else -> throw exception
                }
            }.map { readData ->
                readData[key]
            }

    internal fun getBoolValue(
        key: Preferences.Key<Boolean>,
        context: Context
    ): Flow<Boolean?> = context.dataStore.data
        .catch { exception ->
            when (exception) {
                is IOException -> Log.d(
                    "user preferences helper",
                    "something went wrong :(, internet error!!"
                )

                else -> throw exception
            }
        }.map { readData ->
            readData[key]
        }

    internal suspend fun setBoolValue(
        value: Boolean,
        key: Preferences.Key<Boolean>,
        context: Context
    ) {
        context.dataStore.edit { saveData ->
            saveData[key] = value
        }
    }

}


