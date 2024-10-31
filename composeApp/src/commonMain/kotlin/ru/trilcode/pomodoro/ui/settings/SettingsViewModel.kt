package ru.trilcode.pomodoro.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.trilcode.pomodoro.ui.timer.CountdownTimerViewModel
import ru.trilcode.pomodoro.ui.timer.Times
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class SettingsViewModel(
   private val times: Times
) : ViewModel(), KoinComponent {

    var focusing by mutableStateOf(times.focusing.inWholeSeconds.toString())

    var rest by mutableStateOf(times.rest.inWholeSeconds.toString())

    fun saveTimes() = CoroutineScope(Dispatchers.IO).launch{
        times.focusing = focusing.toInt().seconds
        times.rest = rest.toInt().seconds
    }

}