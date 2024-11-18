package ru.trilcode.pomodoro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import ru.trilcode.pomodoro.theme.TimerTheme
import ru.trilcode.pomodoro.ui.NavigateToSettingsButton

@Composable
fun DesktopApp(
    navController: NavHostController,
    isHomeRoute: Boolean
) {
    TimerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.padding(5.dp).zIndex(1f)
            ) {
                if (isHomeRoute) {
                    NavigateToSettingsButton(
                        navController
                    )
                } else {
                    IconButton(
                        onClick = {
/*                            if(navController.currentBackStack.value.size > 2)
                                navController.navigateUp()*/
                            navController.navigateUp()
                        }
                    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                }
            }
            App(navController)
        }
    }
}