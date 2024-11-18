package ru.trilcode.pomodoro.utils

sealed class Platform {
    data object Android : Platform()
    data object Desktop : Platform()
}