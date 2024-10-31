package ru.trilcode.pomodoro

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.KoinApplication
import ru.trilcode.pomodoro.di.koinConfig
import ru.trilcode.pomodoro.theme.TimerTheme

@Composable
fun App(modifier: Modifier = Modifier) {
    KoinApplication(application = {
        koinConfig()
    }) {
        TimerTheme {
            Surface(Modifier.fillMaxSize()) {
                AppNavHost(modifier)
            }
        }
    }
}