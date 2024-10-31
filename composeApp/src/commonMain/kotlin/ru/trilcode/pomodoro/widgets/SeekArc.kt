package ru.trilcode.pomodoro.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import ru.trilcode.pomodoro.theme.Dark
import ru.trilcode.pomodoro.theme.Light

@Suppress("NAME_SHADOWING")
@Composable
fun SeekArc(
    modifier: Modifier = Modifier,
    max: Float = 100f,
    progress: Float = 0f,
    arcColor: Color = Color(if (isSystemInDarkTheme()) Dark.ARC_COLOR else Light.ARC_COLOR),
    progressColor: Color = colorScheme.primary,
    content: @Composable (BoxScope.() -> Unit)? = null
) {
    // Log.d("SeekArc", "Initialising SeekArc")

    val progressSweep = progress.coerceIn(0f, max) / max * 360

    BoxWithConstraints(modifier) {

        val min = min(minHeight, minWidth).let {
            if (it == 0.dp)
                minHeight + minWidth
            else
                it
        }

        Canvas(Modifier.size(min).align(Alignment.Center)) {
            drawArc(
                color = arcColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = 16.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(
                    width = 16.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        content?.let {
            Box(Modifier.size(min)) {
                content()
            }
        }
    }
}