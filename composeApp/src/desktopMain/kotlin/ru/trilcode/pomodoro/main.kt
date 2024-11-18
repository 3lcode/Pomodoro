package ru.trilcode.pomodoro

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.app_name
import pomodoro.composeapp.generated.resources.close
import pomodoro.composeapp.generated.resources.settings
import pomodoro.composeapp.generated.resources.timer
import ru.trilcode.pomodoro.di.desktopKoinConfig
import ru.trilcode.pomodoro.di.koinConfig
import ru.trilcode.pomodoro.ui.timer.TimerNavigationRoute
import ru.trilcode.pomodoro.utils.toRoute

fun main() {
    startKoin {
        koinConfig()
        desktopKoinConfig()
    }
    application {
        val icon = painterResource(Res.drawable.timer)

        var isVisible by remember { mutableStateOf(true) }

        val navController = rememberNavController()

        val currentRoute = navController
            .currentBackStackEntryFlow
            .collectAsState(initial = navController.currentBackStackEntry)

        val isHomeRoute = when (currentRoute.value?.destination?.route) {
            TimerNavigationRoute.toRoute() -> true
            else -> false
        }
        Window(
            onCloseRequest = { isVisible = false },
            visible = isVisible,
            focusable = true,
            title = if (isHomeRoute) stringResource(Res.string.app_name) else stringResource(Res.string.settings),
            resizable = false,
            icon = icon,
            state = rememberWindowState(
                size = DpSize(400.dp, 500.dp),
                isMinimized = false
            ),
        ) {
            DesktopApp(
                navController,
                isHomeRoute
            )
        }

        Tray(
            icon = icon,
            tooltip = stringResource(Res.string.app_name),
            onAction = { isVisible = !isVisible },
            menu = {
                Item(stringResource(Res.string.close), onClick = ::exitApplication)
            }
        )
    }
}