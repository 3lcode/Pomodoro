package ru.trilcode.pomodoro.ui.timer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class TimerState {
    NONE,
    RUNNING,
    STOP
}

enum class TimerRunningState {
    FOCUSING,
    REST
}

@KoinViewModel
class CountdownTimerViewModel(
    val times: Times
): ViewModel() {
    lateinit var scope: CoroutineScope

    var max by mutableStateOf(times.focusing.inWholeSeconds.toFloat())

    private val timerProgress = Animatable(max)
    val progress by timerProgress.asState()

    var timerState by mutableStateOf(TimerState.NONE)
    var timerRunningState by mutableStateOf(TimerRunningState.FOCUSING)

    fun toggleTimerState(): TimerState {
        timerState = if (timerState == TimerState.NONE || timerState == TimerState.STOP)
            TimerState.RUNNING else TimerState.STOP
        return timerState
    }

    fun runTimer() = scope.launch {
        val result = timerProgress.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = (timerProgress.value * 1000).toInt(),
                easing = LinearEasing
            )
        )
        if (result.endReason == AnimationEndReason.Finished)
            resetTimer()
    }

    fun stopTimer() {
        viewModelScope.launch {
            timerProgress.stop()
        }
    }

    fun launchResetTimer() {
        viewModelScope.launch {
            resetTimer()
        }
    }

    suspend fun resetTimer(toggleRunningState: Boolean = true) {
        max = if(toggleRunningState) toggleRunningState() else computeMax()
        timerState = TimerState.NONE
        timerProgress.snapTo(max)
    }

    private fun toggleRunningState(): Float {
        return if (timerRunningState == TimerRunningState.FOCUSING) {
            timerRunningState = TimerRunningState.REST
            times.rest.inWholeSeconds.toFloat()
        } else {
            timerRunningState = TimerRunningState.FOCUSING
            times.focusing.inWholeSeconds.toFloat()
        }
    }

    private fun computeMax(): Float {
        return if (timerRunningState == TimerRunningState.FOCUSING) {
            times.focusing.inWholeSeconds.toFloat()
        } else {
            times.rest.inWholeSeconds.toFloat()
        }
    }
}