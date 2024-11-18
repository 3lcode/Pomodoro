package ru.trilcode.pomodoro.ui.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel
import ru.trilcode.pomodoro.data.TimesRepository
import ru.trilcode.pomodoro.timer.TimerManager
import kotlin.time.Duration

enum class TimerRunningState {
    FOCUSING,
    REST
}

@KoinViewModel
class CountdownTimerViewModel(
  //  val times: Times,
    timesRepository: TimesRepository,
    private val timer: TimerManager
): ViewModel() {

    val focusing = timesRepository.focusing.stateIn(viewModelScope, SharingStarted.Eagerly, TimesRepository.focusingDefault)
    val rest = timesRepository.rest.stateIn(viewModelScope, SharingStarted.Eagerly, TimesRepository.restDefault)

  //  var max by mutableStateOf(times.focusing)
    var max by mutableStateOf(focusing.value)
    init {
        timer.resetTo(max)
        timer.addOnFinish {
            resetTimer()
        }
    }
    val progress = timer.remainingState

    var timerState = timer.state

    var timerRunningState by mutableStateOf(TimerRunningState.FOCUSING)

    fun runTimer() {
        timer.start()
    }

    fun stopTimer() {
        timer.stop()
    }

    fun resetTimer(toggleRunningState: Boolean = true) {
        max = if(toggleRunningState) toggleRunningState() else computeMax()
        timer.resetTo(max)
    }

    private fun toggleRunningState(): Duration {
        return if (timerRunningState == TimerRunningState.FOCUSING) {
            timerRunningState = TimerRunningState.REST
            rest.value
        } else {
            timerRunningState = TimerRunningState.FOCUSING
            focusing.value
        }
    }

    private fun computeMax(): Duration {
        return if (timerRunningState == TimerRunningState.FOCUSING) {
            focusing.value
        } else {
            rest.value
        }
    }
}