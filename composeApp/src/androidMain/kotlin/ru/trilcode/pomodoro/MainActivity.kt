package ru.trilcode.pomodoro

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.trilcode.pomodoro.di.androidKoinConfig
import ru.trilcode.pomodoro.di.koinConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            koinConfig()
            androidKoinConfig()
        }
        enableEdgeToEdge()
        notificationPerm()

        setContent {
            AndroidApp()
        }
    }

    private fun ComponentActivity.notificationPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isPerGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if(!isPerGranted){
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {

                }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}
