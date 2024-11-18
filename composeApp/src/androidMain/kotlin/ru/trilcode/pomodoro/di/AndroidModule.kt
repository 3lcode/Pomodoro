package ru.trilcode.pomodoro.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.trilcode.pomodoro.data.createDataStore
import ru.trilcode.pomodoro.data.timerDataStoreFileName
import ru.trilcode.pomodoro.data.timerDataStoreName
import ru.trilcode.pomodoro.timer.TimerManager
import ru.trilcode.pomodoro.timer.AndroidTimerManager
import ru.trilcode.pomodoro.utils.Platform

val TimerManagerModule = module {
    single<TimerManager> { AndroidTimerManager(androidContext()) }
}

val TimesDataStoreModule = module {
    single<DataStore<Preferences>>(named(timerDataStoreName)) {
        createDataStore(
            androidContext().filesDir.resolve(timerDataStoreFileName).absolutePath
        )
    }
}

val PlatformModule =  module {
    single<Platform> { Platform.Android }
}

fun KoinApplication.androidKoinConfig() {
    modules(
        TimerManagerModule,
        PlatformModule,
        TimesDataStoreModule,
    )
}