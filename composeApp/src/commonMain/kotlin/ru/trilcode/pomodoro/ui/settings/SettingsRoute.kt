package ru.trilcode.pomodoro.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.focus_duration
import pomodoro.composeapp.generated.resources.minute_abbreviated
import pomodoro.composeapp.generated.resources.rest_duration
import ru.trilcode.pomodoro.utils.Platform
import ru.trilcode.pomodoro.utils.Logger

@Composable
fun SettingsRoute(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val platform = koinInject<Platform>()
    val focusing by settingsViewModel.settings.focusing.collectAsState()
    val rest by settingsViewModel.settings.rest.collectAsState()

    LaunchedEffect(focusing) {
        settingsViewModel.uiState.focusing = focusing.inWholeSeconds.toString() //todo minutes
    }

    LaunchedEffect(focusing) {
        settingsViewModel.uiState.rest = rest.inWholeSeconds.toString() //todo minutes
    }

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_STOP,
        onEvent = {
            settingsViewModel.saveTimes()
        }
    )

    Box(
        Modifier
            .fillMaxSize()
            .padding(top = if(platform is Platform.Desktop) 60.dp else 0.dp)
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                value = settingsViewModel.uiState.focusing,
                onValueChange = { input->
                    if(input.all { it.isDigit() })
                        settingsViewModel.uiState.focusing = input
                },
                label = {
                    Text(
                        text = stringResource(Res.string.focus_duration)
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                suffix = {
                    Text(stringResource(Res.string.minute_abbreviated))
                },
                singleLine = true,
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                value = settingsViewModel.uiState.rest,
                onValueChange = { input ->
                    if(input.all { it.isDigit() })
                        settingsViewModel.uiState.rest = input
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                label = {
                    Text(
                        text = stringResource(Res.string.rest_duration)
                    )
                },
                suffix = {
                    Text(stringResource(Res.string.minute_abbreviated))
                },
                singleLine = true,
            )
        }
    }
}