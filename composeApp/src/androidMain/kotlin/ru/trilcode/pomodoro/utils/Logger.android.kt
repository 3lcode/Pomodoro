package ru.trilcode.pomodoro.utils

import android.util.Log

actual object Logger {
    actual fun log(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}