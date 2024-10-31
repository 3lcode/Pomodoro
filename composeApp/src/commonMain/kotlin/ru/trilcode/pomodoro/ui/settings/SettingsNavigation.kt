package ru.trilcode.pomodoro.ui.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable

@Serializable
object SettingNavigationRoute

fun NavHostController.navigateToSettings() {
    this.navigate(SettingNavigationRoute, navOptions {
        launchSingleTop = true
    })
}

fun NavGraphBuilder.settingScreen(
    navController: NavHostController
) {
    composable<SettingNavigationRoute> {
        SettingsRoute(navController)
    }
}