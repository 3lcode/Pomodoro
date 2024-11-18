package ru.trilcode.pomodoro
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinContext
import ru.trilcode.pomodoro.theme.TimerTheme

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    KoinContext {
        TimerTheme {
            Surface(Modifier.fillMaxSize()) {
                AppNavHost(modifier, navController)
            }
        }
    }
}