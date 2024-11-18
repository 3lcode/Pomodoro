package ru.trilcode.pomodoro.timer

import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration

class DefaultTimerManager : TimerManager, KoinComponent {
    private val timer: Timer by inject()

    override val remainingState: StateFlow<Duration>
        get() = timer.remainingState

    override val state: StateFlow<TimerState>
        get() = timer.state

    override fun start() {
        timer.start()
    }

    override fun stop() {
        timer.stop()
    }

    override fun resetTo(initialTime: Duration) {
        timer.resetTo(initialTime)
    }

    override fun addOnFinish(onFinish: () -> Unit) {
        timer.addOnFinish(onFinish)
    }

    override fun updateState(state: TimerState) {
        timer.updateState(state)
    }
}