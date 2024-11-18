package ru.trilcode.pomodoro.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@Single
class TimesRepository(
    @Named("times") private val dataStore: DataStore<Preferences>
){
    companion object TimesData {
        const val FOCUSING = "focusing"
        const val REST = "rest"
        val focusingDefault = 25.seconds
        val restDefault = 5.seconds
    }

    val focusing = getLong(FOCUSING, focusingDefault)

    val rest = getLong(REST, restDefault)

    suspend fun updateFocusing(value: Long) = putLong(FOCUSING, value)
    suspend fun updateRest(value: Long) = putLong(REST, value)

    private suspend fun putLong(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }

    private fun getLong(key: String, default: Duration): Flow<Duration> = dataStore.data
        .map { preferences ->
            preferences[longPreferencesKey(key)]?.seconds ?: default // todo minutes
        }
}