package ru.trilcode.pomodoro.timer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface TimerManager {
    val remainingState: StateFlow<Duration>
    val state: StateFlow<TimerState>

    fun start()

    fun stop()

    fun resetTo(initialTime: Duration)

    fun addOnFinish(onFinish: () -> Unit)

    fun updateState(state: TimerState)
}