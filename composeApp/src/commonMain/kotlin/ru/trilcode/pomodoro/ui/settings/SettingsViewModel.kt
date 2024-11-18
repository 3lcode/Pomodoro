package ru.trilcode.pomodoro.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.trilcode.pomodoro.data.TimesRepository
import kotlin.time.Duration.Companion.minutes

@KoinViewModel
class SettingsViewModel(
    private val timesRepository: TimesRepository,
   //private val times: Times
) : ViewModel() {
    val settings = Settings()

    val uiState = UiState()

    inner class Settings {
        val focusing = timesRepository.focusing.stateIn(viewModelScope, SharingStarted.Eagerly, TimesRepository.focusingDefault)
        val rest = timesRepository.rest.stateIn(viewModelScope, SharingStarted.Eagerly, TimesRepository.restDefault)
    }

    inner class UiState {
        var focusing by mutableStateOf(TimesRepository.focusingDefault.inWholeSeconds.toString())
        var rest by mutableStateOf(TimesRepository.restDefault.inWholeSeconds.toString())
    }


    fun saveTimes() {
        CoroutineScope(Dispatchers.IO).launch {
            // times.focusing = focusing.toInt().seconds
            //  times.rest = rest.toInt().seconds
            timesRepository.updateFocusing(uiState.focusing.toInt().minutes.inWholeMinutes)
            timesRepository.updateRest(uiState.rest.toInt().minutes.inWholeMinutes)
        }
    }
}