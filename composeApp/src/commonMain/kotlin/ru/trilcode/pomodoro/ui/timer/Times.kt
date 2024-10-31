package ru.trilcode.pomodoro.ui.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.koin.core.annotation.Singleton
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Singleton
class Times {
    var focusing by mutableStateOf(5.seconds/*25.minutes*/)
    var rest by mutableStateOf(2.seconds/*5.minutes*/)
}