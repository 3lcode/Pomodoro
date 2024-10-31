package ru.trilcode.pomodoro.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pomodoro.composeapp.generated.resources.*
import ru.trilcode.pomodoro.ui.settings.navigateToSettings
import ru.trilcode.pomodoro.widgets.SeekArc
import kotlin.math.ceil
import kotlin.time.Duration.Companion.seconds

@Composable
fun CountdownTimerRoute(
    navController: NavHostController,
    viewModel: CountdownTimerViewModel = koinViewModel()
) {

    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        viewModel.scope = scope
    }

    LaunchedEffect(viewModel.times.focusing, viewModel.times.rest) {
        if(viewModel.timerState == TimerState.NONE) {
            viewModel.resetTimer(false)
        }
    }

    Box(Modifier.fillMaxSize()) {
        IconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = {
                navController.navigateToSettings()
            }
        ) {
            Icon(Icons.Default.Settings,"settings")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
        ) {
            SeekArc(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(15.dp)
                    .fillMaxWidth(),
                progress = viewModel.progress,
                max = viewModel.max
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "%02d:%02d".format(
                        ceil(viewModel.progress).toDouble().seconds.inWholeMinutes,
                        ceil(viewModel.progress).toDouble().seconds.inWholeSeconds % 60
                    ),
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 100.sp
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    containerColor = colorScheme.primary,
                    onClick = {
                        val timerState = viewModel.toggleTimerState()
                        if(timerState != TimerState.STOP)
                            viewModel.runTimer()
                        else
                            viewModel.stopTimer()
                    }
                ) {
                    Text(
                        text = when (viewModel.timerState) {
                            TimerState.RUNNING -> stringResource(Res.string.pause)
                            TimerState.STOP -> stringResource(Res.string.`continue`)
                            else -> stringResource(Res.string.start) + " " + if (viewModel.timerRunningState == TimerRunningState.FOCUSING)
                                stringResource(Res.string.focusing) else stringResource(Res.string.`break`)
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (viewModel.timerState == TimerState.STOP) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        containerColor = colorScheme.primary,
                        onClick = {
                            viewModel.launchResetTimer()
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.stop),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}
