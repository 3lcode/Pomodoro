package ru.trilcode.pomodoro

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.app_name
import pomodoro.composeapp.generated.resources.settings
import ru.trilcode.pomodoro.di.TimerManagerModule
import ru.trilcode.pomodoro.di.koinConfig
import ru.trilcode.pomodoro.theme.TimerTheme
import ru.trilcode.pomodoro.ui.NavigateToSettingsButton
import ru.trilcode.pomodoro.ui.timer.TimerNavigationRoute
import ru.trilcode.pomodoro.utils.toRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidApp() {
    val navController = rememberNavController()
    TimerTheme {
        Scaffold(
            topBar = {
                val currentRoute = navController
                    .currentBackStackEntryFlow
                    .collectAsState(initial = navController.currentBackStackEntry)

                val isHomeRoute = when (currentRoute.value?.destination?.route) {
                    TimerNavigationRoute.toRoute() -> true
                    else -> false
                }
                TopAppBar(
                    navigationIcon = {
                        if (!isHomeRoute) {
                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                }
                            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                        }
                    },
                    title = {
                        Text(
                            text = if (isHomeRoute) stringResource(Res.string.app_name) else stringResource(
                                Res.string.settings
                            )
                        )
                    },
                    actions = {
                        if (isHomeRoute)
                            NavigateToSettingsButton(navController)
                    }
                )
            }
        ) { innerPadding ->
            App(
                navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        koinConfig()
        modules(TimerManagerModule)
    }
    AndroidApp()
}