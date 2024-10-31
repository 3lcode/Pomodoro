package ru.trilcode.pomodoro

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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pomodoro.composeapp.generated.resources.*
fun main() = application {

    val icon = painterResource(Res.drawable.timer)

    var isVisible by remember { mutableStateOf(true) }

    Window(
        onCloseRequest = { isVisible = false },
        visible  = isVisible,
        focusable = true,
        title = stringResource(Res.string.app_name),
        resizable = false,
        icon = icon,
        state = rememberWindowState(
            size = DpSize(400.dp, 500.dp),
            isMinimized = false
        ),
    ) {
        App()
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