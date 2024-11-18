package ru.trilcode.pomodoro.ui.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TimerNavigationRoute

fun NavGraphBuilder.timerScreen() {
    composable<TimerNavigationRoute> {
        CountdownTimerRoute()
    }
}

