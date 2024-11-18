package ru.trilcode.pomodoro.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ru.trilcode.pomodoro.ui.settings.navigateToSettings

@Composable
fun NavigateToSettingsButton(
    navController: NavHostController,
) {
    IconButton(
        onClick = {
            navController.navigateToSettings()
        }
    ) {
        Icon(Icons.Default.Settings,"settings")
    }
}