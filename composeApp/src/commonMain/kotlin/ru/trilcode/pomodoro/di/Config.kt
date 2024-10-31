package ru.trilcode.pomodoro.di

import org.koin.core.KoinApplication
import org.koin.ksp.generated.module

fun KoinApplication.koinConfig() {
    modules(
        AppModule().module,
    )
}