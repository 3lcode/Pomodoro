package ru.trilcode.pomodoro.timer

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Single
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

enum class TimerState {
    NONE,
    RUNNING,
    STOP
}

@Single
class Timer(
    private val period: Duration = 10.milliseconds,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var initialTime = Duration.ZERO

    private val onTicks = ArrayList<suspend (Duration) -> Unit>()
    private val onFinishes = ArrayList<suspend () -> Unit>()

    private val _state = MutableStateFlow(TimerState.NONE)
    val state = _state.asStateFlow()

    private val scope = CoroutineScope(dispatcher)
    private var timerJob: Job? = null

    private val _remainingState = MutableStateFlow(Duration.ZERO)
    val remainingState = _remainingState.asStateFlow()

    fun start() {
        timerJob?.cancel()
        timerJob = scope.launch {
            _state.value = TimerState.RUNNING
            while (_remainingState.value > Duration.ZERO) {
                if(isActive) {
                    onTicks.forEach { onTick ->
                        launch {
                            onTick(_remainingState.value)
                        }
                    }
                    delay(period)
                    _remainingState.value -= period
                }
            }
            _state.value = TimerState.NONE
            onFinishes.forEach { onFinish ->
                launch {
                    onFinish()
                }
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        _state.value = TimerState.STOP
    }

    fun cancel() {
        timerJob?.cancel()
        _state.value = TimerState.NONE
    }

    fun reset() {
        resetTo(initialTime)
    }

    fun resetTo(initialTime: Duration) {
        cancel()
        this.initialTime = initialTime
        _remainingState.value = initialTime
    }

    fun addOnTick(onTick: suspend (Duration) -> Unit) {
        onTicks.add(onTick)
    }

    fun addOnFinish(onFinish: suspend () -> Unit) {
        onFinishes.add(onFinish)
    }

    fun updateState(state: TimerState) {
        _state.value = state
    }
}

