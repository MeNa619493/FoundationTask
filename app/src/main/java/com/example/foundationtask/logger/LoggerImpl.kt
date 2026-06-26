package com.example.foundationtask.logger

import android.util.Log

class LoggerImpl : Logger {
    override fun logError(throwable: Throwable) {
        Log.d("LoggerImpl", "${throwable.message}", throwable)
    }
}