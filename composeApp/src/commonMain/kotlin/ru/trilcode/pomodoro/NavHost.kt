package ru.trilcode.pomodoro

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.trilcode.pomodoro.ui.settings.settingScreen
import ru.trilcode.pomodoro.ui.timer.TimerNavigationRoute
import ru.trilcode.pomodoro.ui.timer.timerScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = TimerNavigationRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        timerScreen()
        settingScreen()
    }
}