package ru.trilcode.pomodoro.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.focus_duration
import pomodoro.composeapp.generated.resources.rest_duration

@Composable
fun SettingsRoute(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    Box(Modifier.fillMaxSize()) {
        Column {
            IconButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = {
                    settingsViewModel.saveTimes()
                    if(navController.currentBackStack.value.size > 2)
                        navController.navigateUp()
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "back")
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                value = settingsViewModel.focusing,
                onValueChange = {
                    settingsViewModel.focusing = it
                },
                label = {
                    Text(
                        text = stringResource(Res.string.focus_duration)
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                value = settingsViewModel.rest,
                onValueChange = {
                    settingsViewModel.rest = it
                },
                label = {
                    Text(
                        text = stringResource(Res.string.rest_duration)
                    )
                }
            )
        }
    }
}