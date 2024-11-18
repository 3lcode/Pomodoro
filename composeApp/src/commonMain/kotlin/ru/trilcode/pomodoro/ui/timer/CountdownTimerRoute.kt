package ru.trilcode.pomodoro.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.`break`
import pomodoro.composeapp.generated.resources.`continue`
import pomodoro.composeapp.generated.resources.focusing
import pomodoro.composeapp.generated.resources.pause
import pomodoro.composeapp.generated.resources.start
import pomodoro.composeapp.generated.resources.stop
import ru.trilcode.pomodoro.timer.TimerState
import ru.trilcode.pomodoro.widgets.SeekArc
import kotlin.math.ceil
import kotlin.time.Duration.Companion.seconds

@Composable
fun CountdownTimerRoute(
    viewModel: CountdownTimerViewModel = koinViewModel()
) {
    val progress by viewModel.progress.collectAsState()
    val focusing by viewModel.focusing.collectAsState()
    val rest by viewModel.rest.collectAsState()

    val timerState by viewModel.timerState.collectAsState()

    LaunchedEffect(focusing, rest) {
        if(timerState == TimerState.NONE) {
            viewModel.resetTimer(false)
        }
    }

    Box(Modifier.fillMaxSize()) {
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
                progress = progress.inWholeMilliseconds.toFloat(),
                max = viewModel.max.inWholeMilliseconds.toFloat()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "%02d:%02d".format(
                        ceil(progress.inWholeMilliseconds / 1000.0).seconds.inWholeMinutes,
                        ceil(progress.inWholeMilliseconds / 1000.0).seconds.inWholeSeconds % 60
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
                        if(timerState == TimerState.RUNNING)
                            viewModel.stopTimer()
                        else
                            viewModel.runTimer()
                    }
                ) {
                    Text(
                        text = when (timerState) {
                            TimerState.RUNNING -> stringResource(Res.string.pause)
                            TimerState.STOP -> stringResource(Res.string.`continue`)
                            else -> stringResource(Res.string.start) + " " + if (viewModel.timerRunningState == TimerRunningState.FOCUSING)
                                stringResource(Res.string.focusing) else stringResource(Res.string.`break`)
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                if (timerState == TimerState.STOP) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        containerColor = colorScheme.primary,
                        onClick = {
                            viewModel.resetTimer()
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
