package ru.trilcode.pomodoro.timer

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.trilcode.pomodoro.TimerService
import kotlin.time.Duration

class AndroidTimerManager (
    private val context: Context
) : TimerManager, KoinComponent {

    companion object {
        const val STATE_NAME = "state"
    }

    enum class State {
        START,
        STOP
    }

    private val timer: Timer by inject()

    override val remainingState: StateFlow<Duration>
        get() = timer.remainingState

    override val state: StateFlow<TimerState>
        get() = timer.state

    override fun start() {
        context.startService(Intent(context, TimerService::class.java).apply {
            putExtra(STATE_NAME, State.START)
        })
    }

    override fun stop() {
        context.startService(Intent(context, TimerService::class.java).apply {
            putExtra(STATE_NAME, State.STOP)
        })
    }

    override fun resetTo(initialTime: Duration) {
        context.stopService(Intent(context, TimerService::class.java))
        timer.resetTo(initialTime)
    }

    override fun addOnFinish(onFinish: () -> Unit) {
        timer.addOnFinish(onFinish)
    }

    override fun updateState(state: TimerState) {
        timer.updateState(state)
    }
}