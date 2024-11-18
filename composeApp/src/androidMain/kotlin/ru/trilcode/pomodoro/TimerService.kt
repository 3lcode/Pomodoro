package ru.trilcode.pomodoro

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.koin.android.ext.android.inject
import pomodoro.composeapp.generated.resources.Res
import pomodoro.composeapp.generated.resources.`continue`
import pomodoro.composeapp.generated.resources.minutes
import pomodoro.composeapp.generated.resources.pause
import pomodoro.composeapp.generated.resources.remaining_time_notification
import pomodoro.composeapp.generated.resources.seconds
import pomodoro.composeapp.generated.resources.stop
import pomodoro.composeapp.generated.resources.timer
import pomodoro.composeapp.generated.resources.timer_completed
import pomodoro.composeapp.generated.resources.timer_notification_channel
import ru.trilcode.pomodoro.timer.AndroidTimerManager
import ru.trilcode.pomodoro.timer.AndroidTimerManager.Companion.STATE_NAME
import ru.trilcode.pomodoro.timer.Timer
import ru.trilcode.pomodoro.timer.TimerState
import kotlin.time.Duration

private typealias State = AndroidTimerManager.State

class TimerService : Service() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "TimerServiceChannel"
        const val NOTIFICATION_ID = 1

        private const val NOTIFICATION_ACTION = "FROM_NOTIFICATION"
        private const val NOTIFICATION_ACTION_NAME = "action"
        private enum class NotificationActions {
            STOP_TIMER,
            CONTINUE_TIMER,
            RESET_TIMER
        }
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val timer: Timer by inject()

    init {
        timer.apply {
            addOnTick { remainingTime ->
                if(remainingTime.inWholeMilliseconds % 1000 == 0L) {
                    notify(remainingTime.toNotifyText())
                }
            }
            addOnFinish {
                notify(getString(Res.string.timer_completed), true)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            createNotificationChannel()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(timer.state.value == TimerState.NONE) {
            serviceScope.launch {
                startForeground(
                    NOTIFICATION_ID,
                    createNotification(timer.remainingState.value.toNotifyText())
                )
            }
        }

        if(intent!!.action == NOTIFICATION_ACTION) {
            val action = intent.getSerializableExtra(NOTIFICATION_ACTION_NAME) as NotificationActions
            when (action) {
                NotificationActions.STOP_TIMER -> {
                    timer.stop()
                    serviceScope.launch {
                        notify(timer.remainingState.value.toNotifyText())
                    }
                }
                NotificationActions.CONTINUE_TIMER -> {
                    timer.start()
                    serviceScope.launch {
                        notify(timer.remainingState.value.toNotifyText())
                    }
                }
                NotificationActions.RESET_TIMER -> {
                    timer.reset()
                    stopSelf()
                }
            }
        } else {
            val state = intent.getSerializableExtra(STATE_NAME) as State
            if (state == State.START) {
                timer.start()
            } else if (state == State.STOP) {
                timer.stop()
            }
        }

        return START_STICKY
    }

    private suspend fun createNotification(
        contentText: String,
        isFinished: Boolean = false
    ): Notification {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // reset
        val resetIntent = Intent(this, TimerService::class.java).apply {
            action = NOTIFICATION_ACTION
            putExtra(NOTIFICATION_ACTION_NAME, NotificationActions.RESET_TIMER)
        }

        val resetPendingIntent: PendingIntent = PendingIntent.getService(this, 1, resetIntent,  PendingIntent.FLAG_IMMUTABLE)

        //stop
        val stopIntent = Intent(this, TimerService::class.java).apply {
            action = NOTIFICATION_ACTION
            putExtra(NOTIFICATION_ACTION_NAME, NotificationActions.STOP_TIMER)
        }

        val stopPendingIntent: PendingIntent = PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseAction = Action(
            android.R.drawable.screen_background_light_transparent,
            getString(Res.string.pause), stopPendingIntent
        )

        // continue
        val continueIntent = Intent(this, TimerService::class.java).apply {
            action = NOTIFICATION_ACTION
            putExtra(NOTIFICATION_ACTION_NAME, NotificationActions.CONTINUE_TIMER)
        }

        val continuePendingIntent: PendingIntent = PendingIntent.getService(this, 3, continueIntent, PendingIntent.FLAG_IMMUTABLE)


        val continueAction = Action(
            android.R.drawable.screen_background_light_transparent,
            getString(Res.string.`continue`), continuePendingIntent
        )

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.timer_big)
            .setShowWhen(false)
            .setContentTitle(getString(Res.string.timer))
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openPendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(!isFinished)

        if(!isFinished) {
            builder
                .addAction(android.R.drawable.screen_background_light_transparent, getString(Res.string.stop), resetPendingIntent)
                .addAction(if(timer.state.value == TimerState.STOP) continueAction else pauseAction)
        }
        return builder.build()
    }

    private suspend fun Duration.toNotifyText(): String {
        val content = if(inWholeMinutes >= 5) {
            "$inWholeMinutes " + getPluralString(Res.plurals.minutes, inWholeMinutes.toInt())
        } else if(inWholeMinutes > 0) {
            val seconds = inWholeSeconds % 60
            "$inWholeMinutes " + getPluralString(Res.plurals.minutes, inWholeMinutes.toInt()) +
            " $seconds " + getPluralString(Res.plurals.seconds, seconds.toInt())
        } else {
            "$inWholeSeconds " + getPluralString(Res.plurals.seconds, inWholeSeconds.toInt())
        }
        return getString(
            Res.string.remaining_time_notification, content)
    }

    private fun notify(
        contentText: String,
        isFinished: Boolean = false
    ) {
        serviceScope.launch {
            val notification = createNotification(contentText, isFinished)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(Res.string.timer_notification_channel),
            NotificationManager.IMPORTANCE_HIGH // IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun getString(resource: StringResource) = org.jetbrains.compose.resources.getString(resource)

    private suspend fun getString(resource: StringResource, vararg formatArgs: Any) = org.jetbrains.compose.resources.getString(resource, *formatArgs)
}